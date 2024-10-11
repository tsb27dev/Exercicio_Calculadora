package pt.ipca.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.text.font.FontWeight
import pt.ipca.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                Calculator()
            }
        }
    }
}

@Composable
fun Calculator() {
    var digitos by remember { mutableStateOf("0") }
    var primeiroValor by remember { mutableStateOf(0.0) }
    var operador by remember { mutableStateOf("") }
    var currentInput by remember { mutableStateOf("") }
    var valorGuardado by remember { mutableStateOf(0.0) }


    Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
                .padding(top = 60.dp, start = 15.dp, end = 15.dp, bottom = 55.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        )
         {
                // Display
                Text(
                    text = digitos,
                    fontSize = 48.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFB6CBB5))
                        .border(width = 2.dp, color = Color.Black)
                        .padding(16.dp),
                    textAlign = TextAlign.End,
                    color = Color.Black
                )


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(), // Adjust to wrap the content height instead of filling the parent
                verticalArrangement = Arrangement.spacedBy(8.dp) // Manage the space between rows
            ) {

                val botoes = listOf(
                    listOf("MRC", "M-", "M+", "ON/C"),
                    listOf("√", "%", "+/-", "CE"),
                    listOf("7", "8", "9", "/"),
                    listOf("4", "5", "6", "*"),
                    listOf("1", "2", "3", "-"),
                    listOf("0", ".", "=", "+")
                )

                for (row in botoes) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (textoBotao in row) {
                            val corBotao = when (textoBotao) {
                                "CE", "ON/C" -> ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFE25778), // Rosado para "clear"
                                    contentColor = Color.White
                                )
                                "/", "*", "+", "-", "MRC", "M+", "M-", "%", "+/-", "√" -> ButtonDefaults.buttonColors(
                                    containerColor = Color.Black, // Preto para operadores
                                    contentColor = Color.White
                                )
                                else -> ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF727372), // Cinzento para números
                                    contentColor = Color.White
                                )
                            }
                            Button(
                                onClick = {
                                    handleButtonClick(
                                        textoBotao,
                                        digitos,
                                        currentInput,
                                        primeiroValor,
                                        operador,
                                        valorGuardado,
                                        setValorGuardado = { valorGuardado = it },
                                        setDigitos = { digitos = it },
                                        setInput = { currentInput = it },
                                        setPrimeiroValor = { primeiroValor = it },
                                        setOperador = { operador = it }
                                    )
                                },
                                modifier = Modifier
                                    .width(95.dp)
                                    .height(60.dp)
                                    .padding(2.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = corBotao
                            ) {
                                Text(
                                    text = textoBotao,
                                    softWrap = false,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

        }
    }


fun handleButtonClick(
    textoBotao: String,
    digitos: String,
    input: String,
    primeiroValor: Double,
    operador: String,
    valorGuardado: Double,
    setValorGuardado: (Double) -> Unit,
    setDigitos: (String) -> Unit,
    setInput: (String) -> Unit,
    setPrimeiroValor: (Double) -> Unit,
    setOperador: (String) -> Unit
) {
    fun formatarResultado(result: Double): String {
        return if (result % 1 == 0.0) {
            result.toInt().toString()
        } else {
            String.format("%.8f", result).trimEnd('0').trimEnd('.')
        }
    }

    fun formatarValorGuardado(value: Double): String {
        return if (value % 1 == 0.0) {
            value.toInt().toString()
        } else {
            value.toString()
        }
    }

    when (textoBotao) {
        "MRC" -> {
            setDigitos(formatarValorGuardado(valorGuardado))
            setInput(formatarValorGuardado(valorGuardado))
        }
        "M+" -> {
            val currentValue = input.toDoubleOrNull() ?: 0.0
            val newMemoryValue = valorGuardado + currentValue
            setValorGuardado(newMemoryValue)
            setDigitos("M+")
        }
        "M-" -> {
            val currentValue = input.toDoubleOrNull() ?: 0.0
            val newMemoryValue = valorGuardado - currentValue
            setValorGuardado(newMemoryValue)
            setDigitos("M-")
        }

        "√" -> {
            val valor = input.toDoubleOrNull() ?: 0.0
            val res = kotlin.math.sqrt(valor)
            val resultadoFormatado = formatarResultado(res)
            setDigitos(resultadoFormatado)
            setInput(resultadoFormatado)
        }
        "%" -> {
            val currentInput = input.toDoubleOrNull() ?: 0.0
            if (primeiroValor != null && operador != null) {
                val percentagem = (primeiroValor * currentInput) / 100
                val percentagemFormatada = formatarResultado(percentagem)
                setDigitos(percentagemFormatada)
                setInput(percentagemFormatada)
            } else {
                val res = currentInput / 100
                val resultadoFormatado = formatarResultado(res)
                setDigitos(resultadoFormatado)
                setInput(resultadoFormatado)
            }
        }
        "+/-" -> {
            val valor = input.toDoubleOrNull() ?: 0.0
            val res = valor * -1
            val resultadoFormatado = formatarResultado(res)
            setDigitos(resultadoFormatado)
            setInput(resultadoFormatado)
        }
        "ON/C" -> {
            setInput("")
            setDigitos("0")
        }
        "CE" -> {
            if (input.isNotEmpty()) {
                val newInput = input.dropLast(1)
                setInput(newInput)
                setDigitos(newInput.ifEmpty { "0" })
            }
        }
        in "0".."9", "." -> {
            val newInput = input + textoBotao
            setInput(newInput)
            setDigitos(newInput)
        }
        "+", "-", "*", "/" -> {
            setPrimeiroValor(input.toDouble())
            setInput("")
            setOperador(textoBotao)
        }
        "=" -> {
            val segundoValor = input.toDouble()
            val res = when (operador) {
                "+" -> primeiroValor + segundoValor
                "-" -> primeiroValor - segundoValor
                "*" -> primeiroValor * segundoValor
                "/" -> primeiroValor / segundoValor
                else -> 0.0
            }

            val resultadoFormatado = formatarResultado(res)

            setDigitos(resultadoFormatado)
            setInput(resultadoFormatado)
        }
    }
}
