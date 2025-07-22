import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MA_Morph(
	modifier: Modifier = Modifier,
	size: Dp = 48.dp,
	color: Color = MaterialTheme.colorScheme.primary
) {
	// 1. Configura una transición infinita para la animación en bucle.
	val infiniteTransition: InfiniteTransition = rememberInfiniteTransition(label = "morph_transition")

	// 2. Anima el porcentaje de las esquinas de 50% (círculo) a 25% (squircle) y viceversa.
	val cornerRadiusPercent by infiniteTransition.animateFloat(
		initialValue = 50f,
		targetValue = 25f,
		animationSpec = infiniteRepeatable(
			animation = androidx.compose.animation.core.tween(
				durationMillis = 1000,
				easing = CubicBezierEasing(0.4f, 0.0f, 0.6f, 1.0f) // Easing suave
			),
			repeatMode = RepeatMode.Reverse // Invierte la animación en cada ciclo
		),
		label = "corner_radius_animation"
	)

	// 3. Anima la rotación para añadir más dinamismo.
	val rotation by infiniteTransition.animateFloat(
		initialValue = 0f,
		targetValue = 90f,
		animationSpec = infiniteRepeatable(
			animation = androidx.compose.animation.core.tween(
				durationMillis = 1000,
				easing = CubicBezierEasing(0.4f, 0.0f, 0.6f, 1.0f)
			),
			repeatMode = RepeatMode.Reverse
		),
		label = "rotation_animation"
	)

	// 4. Dibuja la forma utilizando los valores animados.
	Box(
		modifier = modifier
			.size(size)
			.rotate(rotation) // Aplica la rotación animada
			.background(
				color = color,
				// El truco principal: anima el porcentaje de las esquinas
				shape = RoundedCornerShape(percent = cornerRadiusPercent.toInt())
			)
	)
}

@Preview(showBackground = true)
@Composable
private fun MorphingLoadingIndicatorPreview() {
	MaterialTheme {
		MA_Morph()
	}
}