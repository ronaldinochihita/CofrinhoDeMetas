package pt.ipbeja.cofrinho

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import pt.ipbeja.cofrinho.ui.navigation.CofrinhoNavHost
import pt.ipbeja.cofrinho.ui.theme.CofrinhoDeMetasTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val app = application as CofrinhoApp
        val repository = app.repository

        setContent {
            CofrinhoDeMetasTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CofrinhoNavHost(repository = repository)
                }
            }
        }
    }
}
