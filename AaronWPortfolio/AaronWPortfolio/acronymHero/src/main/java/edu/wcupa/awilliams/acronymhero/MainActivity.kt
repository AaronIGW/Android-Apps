package edu.wcupa.awilliams.acronymhero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import edu.wcupa.awilliams.acronymhero.ui.AcronymHeroApp
import edu.wcupa.awilliams.acronymhero.ui.theme.AcronymHeroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AcronymHeroTheme {
                Column(Modifier.fillMaxSize()) {
                    Button(onClick = { finish() }) {
                        Text("Return Home")
                    }
                    AcronymHeroApp()
                }
            }
        }
    }
}
