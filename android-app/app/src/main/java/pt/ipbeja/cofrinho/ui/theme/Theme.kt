package pt.ipbeja.cofrinho.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val EsquemaClaro = lightColorScheme(
    primary = VerdeCofre,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = VerdeCofreClaro,
    onPrimaryContainer = VerdeCofreEscuro,
    secondary = DouradoAcento,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = DouradoSuave,
    onSecondaryContainer = androidx.compose.ui.graphics.Color(0xFF5D4037),
    background = CinzaFundoClaro,
    onBackground = CinzaTexto,
    surface = androidx.compose.ui.graphics.Color.White,
    onSurface = CinzaTexto,
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFFECEFEA),
    onSurfaceVariant = CinzaTextoSuave,
    error = VermelhoAlerta
)

private val EsquemaEscuro = darkColorScheme(
    primary = VerdeCofreClaro,
    onPrimary = VerdeCofreEscuro,
    primaryContainer = VerdeCofreEscuro,
    onPrimaryContainer = VerdeCofreClaro,
    secondary = DouradoAcento,
    onSecondary = androidx.compose.ui.graphics.Color.Black,
    background = CinzaFundoEscuro,
    onBackground = androidx.compose.ui.graphics.Color(0xFFECEFEA),
    surface = androidx.compose.ui.graphics.Color(0xFF1B1D1B),
    onSurface = androidx.compose.ui.graphics.Color(0xFFECEFEA),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFF2A2C2A),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFFBEC5BE),
    error = androidx.compose.ui.graphics.Color(0xFFEF9A9A)
)

@Composable
fun CofrinhoDeMetasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val esquema = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val ctx = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
        }
        darkTheme -> EsquemaEscuro
        else -> EsquemaClaro
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = esquema.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    MaterialTheme(
        colorScheme = esquema,
        typography = CofrinhoTypography,
        content = content
    )
}
