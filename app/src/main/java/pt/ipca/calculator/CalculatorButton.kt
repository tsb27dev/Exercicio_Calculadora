package pt.ipca.calculator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import pt.ipca.calculator.ui.theme.CalculatorTheme
import pt.ipca.calculator.ui.theme.Grey
import pt.ipca.calculator.ui.theme.Pink
import pt.ipca.calculator.ui.theme.Black

@Composable
fun CalculatorButton(
modifier: Modifier = Modifier,
label : String,
isON : Boolean = false,
isOperation : Boolean = false,
onClick: (String) -> Unit
){
    Button(onClick = { onClick(label) },
        modifier = modifier
            .width(100.dp)
            .height(60.dp)
            .padding(2.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isOperation ) Black else if (isON) Pink else Grey
        )
    ) {
        Text(
            label,
            fontSize = TextUnit(value =
                if (label.length > 1 ) 20f else 41f,
                type = TextUnitType.Sp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorButtonPreview(){
    CalculatorTheme {
        CalculatorButton(
            label = "MRC",
            onClick = {}
        )
    }
}
