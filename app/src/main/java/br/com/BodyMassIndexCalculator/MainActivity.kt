package br.com.BodyMassIndexCalculator

import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.BodyMassIndexCalculator.ui.theme.BodyMassIndexCalculatorTheme
import kotlin.math.pow
import kotlin.math.round

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT)
        )
        setContent {
            BodyMassIndexCalculatorTheme {
                BMICalculator()
            }
        }
    }
}

private object Palette {
    val Background = Color(0xFF080E1A)
    val Surface = Color(0xFF101827)
    val Outline = Color(0xFF222E42)
    val TextPrimary = Color(0xFFE8EEF7)
    val TextSecondary = Color(0xFF8593A8)
    val Accent = Color(0xFF3CF2C8)

    val Underweight = Color(0xFF5CB8FF)
    val Healthy = Color(0xFF3CF2C8)
    val Overweight = Color(0xFFFFC15C)
    val Obese = Color(0xFFFF6B7A)
}

private val TechColorScheme = darkColorScheme(
    primary = Palette.Accent,
    onPrimary = Palette.Background,
    background = Palette.Background,
    onBackground = Palette.TextPrimary,
    surface = Palette.Surface,
    onSurface = Palette.TextPrimary,
    error = Palette.Obese
)

private enum class BmiCategory(val label: String, val color: Color) {
    UNDERWEIGHT("Underweight", Palette.Underweight),
    HEALTHY("Healthy Weight", Palette.Healthy),
    OVERWEIGHT("Overweight", Palette.Overweight),
    OBESITY("Obesity", Palette.Obese)
}

private data class BmiResult(val bmi: Double, val category: BmiCategory)

private fun computeBmi(weightKg: Double, heightCm: Double): BmiResult {
    val heightM = heightCm / 100.0
    val bmi = round(weightKg / heightM.pow(2) * 10) / 10.0

    val category = when {
        bmi < 18.5 -> BmiCategory.UNDERWEIGHT
        bmi < 25.0 -> BmiCategory.HEALTHY
        bmi < 30.0 -> BmiCategory.OVERWEIGHT
        else -> BmiCategory.OBESITY
    }
    return BmiResult(bmi, category)
}

private fun String.toNumberOrNull(): Double? = replace(',', '.').toDoubleOrNull()

private val numberInput = Regex("^\\d*[.,]?\\d*$")

@Composable
fun BMICalculator() {
    MaterialTheme(colorScheme = TechColorScheme) {
        var weight by remember { mutableStateOf("") }
        var height by remember { mutableStateOf("") }
        var result by remember { mutableStateOf<BmiResult?>(null) }
        var hasError by remember { mutableStateOf(false) }
        val focusManager = LocalFocusManager.current

        fun calculate() {
            val w = weight.toNumberOrNull()
            val h = height.toNumberOrNull()

            if (w == null || h == null || w <= 0.0 || h <= 0.0) {
                result = null
                hasError = true
                return
            }
            hasError = false
            result = computeBmi(weightKg = w, heightCm = h)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Palette.Accent.copy(alpha = 0.09f),
                        0.4f to Palette.Background,
                        1f to Palette.Background
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Header()

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricField(
                        value = weight,
                        onValueChange = { if (numberInput.matches(it)) weight = it },
                        label = "Weight (KG)",
                        unit = "kg",
                        isError = hasError && weight.toNumberOrNull() == null,
                        imeAction = ImeAction.Next,
                        modifier = Modifier.weight(1f)
                    )
                    MetricField(
                        value = height,
                        onValueChange = { if (numberInput.matches(it)) height = it },
                        label = "Height (CM)",
                        unit = "cm",
                        isError = hasError && height.toNumberOrNull() == null,
                        imeAction = ImeAction.Done,
                        onDone = {
                            focusManager.clearFocus()
                            calculate()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                if (hasError) {
                    Text(
                        text = "Enter your weight in kg and your height in cm.",
                        color = Palette.Obese,
                        fontSize = 13.sp
                    )
                }

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        calculate()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Palette.Accent,
                        contentColor = Palette.Background
                    )
                ) {
                    Text(
                        text = "Calculate",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                }

                AnimatedVisibility(
                    visible = result != null,
                    enter = fadeIn(tween(300)) + slideInVertically(tween(400)) { it / 6 }
                ) {
                    result?.let { ResultCard(it) }
                }
            }
        }
    }
}

@Composable
private fun Header() {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "Body Mass Index Calculator",
            color = Palette.TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            letterSpacing = 4.sp
        )
        Text(
            text = "Insert your weight and height bellow and calculate your Body Mass Index:",
            color = Palette.TextSecondary,
            fontSize = 15.sp
        )
    }
}

@Composable
private fun MetricField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    unit: String,
    isError: Boolean,
    imeAction: ImeAction,
    modifier: Modifier = Modifier,
    onDone: () -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(text = label) },
        suffix = { Text(text = unit, color = Palette.TextSecondary) },
        singleLine = true,
        isError = isError,
        shape = RoundedCornerShape(16.dp),
        textStyle = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 18.sp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Palette.TextPrimary,
            unfocusedTextColor = Palette.TextPrimary,
            focusedContainerColor = Palette.Surface,
            unfocusedContainerColor = Palette.Surface,
            errorContainerColor = Palette.Surface,
            focusedBorderColor = Palette.Accent,
            unfocusedBorderColor = Palette.Outline,
            errorBorderColor = Palette.Obese,
            focusedLabelColor = Palette.Accent,
            unfocusedLabelColor = Palette.TextSecondary,
            errorLabelColor = Palette.Obese,
            cursorColor = Palette.Accent,
            errorCursorColor = Palette.Obese
        )
    )
}

@Composable
private fun ResultCard(result: BmiResult) {
    val animatedBmi = remember { Animatable(0f) }
    LaunchedEffect(result) {
        animatedBmi.animateTo(
            targetValue = result.bmi.toFloat(),
            animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
        )
    }
    val categoryColor by animateColorAsState(
        targetValue = result.category.color,
        label = "categoryColor"
    )

    val shape = RoundedCornerShape(24.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Palette.Surface)
            .border(1.dp, Palette.Outline, shape)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = "%.1f".format(animatedBmi.value),
                color = Palette.TextPrimary,
                fontSize = 64.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Light
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "kg/m²",
                color = Palette.TextSecondary,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 14.dp)
            )
        }

        CategoryChip(label = result.category.label, color = categoryColor)

        BmiGauge(
            bmi = animatedBmi.value,
            markerColor = categoryColor,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Text(
            text = "Adult ranges based on WHO classification.",
            color = Palette.TextSecondary,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun CategoryChip(label: String, color: Color) {
    val chipShape = RoundedCornerShape(50)
    Box(
        modifier = Modifier
            .clip(chipShape)
            .background(color.copy(alpha = 0.14f))
            .border(1.dp, color.copy(alpha = 0.5f), chipShape)
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = color,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun BmiGauge(
    bmi: Float,
    markerColor: Color,
    modifier: Modifier = Modifier
) {
    val bounds = listOf(10f, 18.5f, 25f, 30f, 40f)
    val colors = listOf(
        Palette.Underweight,
        Palette.Healthy,
        Palette.Overweight,
        Palette.Obese
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp)
    ) {
        val barHeight = 6.dp.toPx()
        val gap = 4.dp.toPx()
        val top = (size.height - barHeight) / 2f
        val usableWidth = size.width - gap * (colors.size - 1)
        val totalSpan = bounds.last() - bounds.first()

        val starts = FloatArray(colors.size)
        val widths = FloatArray(colors.size)

        var x = 0f
        for (i in colors.indices) {
            val w = usableWidth * (bounds[i + 1] - bounds[i]) / totalSpan
            starts[i] = x
            widths[i] = w
            drawRoundRect(
                color = colors[i].copy(alpha = 0.35f),
                topLeft = Offset(x, top),
                size = Size(w, barHeight),
                cornerRadius = CornerRadius(barHeight / 2f, barHeight / 2f)
            )
            x += w + gap
        }

        val value = bmi.coerceIn(bounds.first(), bounds.last())
        val segment = (0 until colors.size).firstOrNull { value <= bounds[it + 1] }
            ?: (colors.size - 1)
        val fraction = (value - bounds[segment]) / (bounds[segment + 1] - bounds[segment])
        val center = Offset(
            x = starts[segment] + widths[segment] * fraction,
            y = size.height / 2f
        )

        drawCircle(markerColor.copy(alpha = 0.25f), radius = 11.dp.toPx(), center = center)
        drawCircle(Palette.TextPrimary, radius = 6.dp.toPx(), center = center)
        drawCircle(markerColor, radius = 3.dp.toPx(), center = center)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF080E1A)
@Composable
fun BMICalculatorPreview() {
    BodyMassIndexCalculatorTheme {
        BMICalculator()
    }
}