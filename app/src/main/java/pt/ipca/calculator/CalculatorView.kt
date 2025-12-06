package pt.ipca.calculator


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipca.calculator.ui.theme.CalculatorTheme

@Composable
fun CalculatorView(
    modifier: Modifier = Modifier
) {

    var displayText by remember { mutableStateOf("0") }
    var userIsTypingNumber by remember { mutableStateOf(true) }
    val calculatorBrain by remember { mutableStateOf(CalculatorBrain()) }

    val onNumberClick : (String) -> Unit = { number ->
        if(userIsTypingNumber) {
            if (number == ".") {
                if (!(displayText.contains("."))) {
                    displayText += number
                }
            } else {
                if (displayText == "0") {
                    displayText = number
                } else {
                    displayText += number
                }
            }
        }else{
            displayText = number
        }
    }

    val onOperationClick : (String) -> Unit = { operation ->
        val currentValue = displayText.toDoubleOrNull() ?: 0.0

        val result = calculatorBrain.doOperation(
            currentValue,
            CalculatorBrain.Operation.parse(operation)
        )

        displayText = if (result % 1 == 0.0) {
            result.toInt().toString()
        } else {
            result.toString()
        }

        // Para operações unárias (√, %, +/-), continuar digitando
        userIsTypingNumber = when(operation) {
            "√", "%", "+/-" -> true
            else -> false
        }
    }

    val onMemoryClick : (String) -> Unit = { memory ->
        val currentValue = displayText.toDoubleOrNull() ?: 0.0

        when(memory) {
            "M+" -> {
                calculatorBrain.memoryAdd(currentValue)
            }
            "M-" -> {
                calculatorBrain.memorySubtract(currentValue)
            }
            "MRC" -> {
                val memoryValue = calculatorBrain.memoryRecall()
                displayText = if (memoryValue % 1 == 0.0) {
                    memoryValue.toInt().toString()
                } else {
                    memoryValue.toString()
                }
                userIsTypingNumber = false
            }
        }
    }

    val onClearClick : (String) -> Unit = { clear ->
        when(clear) {
            "CE" -> {
                displayText = "0"
                calculatorBrain.clearEntry()
                userIsTypingNumber = true
            }
            "ON" -> {
                displayText = "0"
                calculatorBrain.reset()
                userIsTypingNumber = true
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(top = 30.dp, start = 15.dp, end = 15.dp, bottom = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(displayText,
            fontSize = 48.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFB6CBB5))
                .border(width = 2.dp, color = Color.Black)
                .padding(16.dp),
            textAlign = TextAlign.End,
            color = Color.Black

        )}

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 130.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row {
            CalculatorButton(label ="MRC", isOperation = true, onClick = onMemoryClick )
            CalculatorButton(label ="M-", isOperation = true, onClick = onMemoryClick )
            CalculatorButton(label ="M+", isOperation = true, onClick = onMemoryClick )
            CalculatorButton(label ="ON", isON = true, onClick = onClearClick )
        }
        Row {
            CalculatorButton(label ="√", isOperation = true, onClick = onNumberClick )
            CalculatorButton(label ="%", isOperation = true, onClick = onNumberClick )
            CalculatorButton(label ="+/-", isOperation = true,  onClick = onNumberClick )
            CalculatorButton(label ="CE", isON = true, onClick = onClearClick )
        }
        Row {
            CalculatorButton(label ="7", onClick = onNumberClick )
            CalculatorButton(label ="8", onClick = onNumberClick )
            CalculatorButton(label ="9", onClick = onNumberClick )
            CalculatorButton(label ="/", isOperation = true, onClick = onOperationClick )
        }
        Row {
            CalculatorButton(label ="4", onClick = onNumberClick )
            CalculatorButton(label ="5", onClick = onNumberClick )
            CalculatorButton(label ="6", onClick = onNumberClick )
            CalculatorButton(label ="x", isOperation = true, onClick = onOperationClick )
        }
        Row {
            CalculatorButton(label ="1", onClick = onNumberClick )
            CalculatorButton(label ="2", onClick = onNumberClick )
            CalculatorButton(label ="3", onClick = onNumberClick )
            CalculatorButton(label ="-", isOperation = true, onClick = onOperationClick )
        }
        Row {
            CalculatorButton(label ="0", onClick = onNumberClick )
            CalculatorButton(label =".", onClick = onNumberClick )
            CalculatorButton(label ="=", isOperation = true, onClick = onOperationClick )
            CalculatorButton(label ="+", isOperation = true, onClick = onOperationClick )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorViewPreview(){
    CalculatorTheme {
        CalculatorView()
    }
}