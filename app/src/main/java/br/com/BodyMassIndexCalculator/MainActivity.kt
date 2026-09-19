package br.com.BodyMassIndexCalculator

import android.R.attr.onClick
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.BodyMassIndexCalculator.ui.theme.BodyMassIndexCalculatorTheme
import kotlin.math.pow
import kotlin.math.round

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BodyMassIndexCalculatorTheme {
                BMICalculator()
                }
            }
        }
}

@Composable
fun BMICalculator() {

    var height by remember { mutableStateOf(value = "") }
    var weight by remember { mutableStateOf(value = "") }
    var result by remember { mutableStateOf(value = "") }

    fun BMICalc() {
        val w = weight.replace(oldValue = ",", newValue = ".").toDoubleOrNull()
        val hCM = height.replace(oldValue = ",", newValue = ".").toDoubleOrNull()

        if (w == null || hCM == null || w <= 0 || hCM <=0) {
            result = "Please fill in the height (CM) and weight (KG) correctly."
            return
        }

        val h = hCM / 100.0
        val imc = w / h.pow(2)
        val imcResult = round(x = imc * 10) / 10.0

        val category = when {
            imcResult < 18.5 -> "Underweight"
            imcResult < 25.0 -> "Healthy Weight"
            imcResult < 30.0 -> "Overweight"
            else -> "Obesity"
        }

        result = "BMI: %.1f\nClassification: %s".format(imcResult, category)

    }

    Column(
        modifier = Modifier.fillMaxSize().padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text="BMI Calculator", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = weight,
            onValueChange = { v -> weight = v.filter { it.isDigit() || it == '.' || it == ','} },
            label = { Text(text = "Weight (KG)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = height,
            onValueChange = { v -> height = v.filter { it.isDigit() || it == '.' || it == ','} },
            label = { Text(text = "Height (CM)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button (
            onClick = { BMICalc() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Calculate")
        }

        if (result.isNotEmpty()) {
            Text(text = result, style = MaterialTheme.typography.bodyLarge)
        }

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BodyMassIndexCalculatorTheme {
        BMICalculator()
    }
}
       