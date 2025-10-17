package edu.wcupa.awilliams.aaronwportfolio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import edu.wcupa.awilliams.aaronwportfolio.ui.PortfolioScreen
import edu.wcupa.awilliams.aaronwportfolio.ui.theme.AaronWPortfolioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AaronWPortfolioTheme(darkTheme = false, dynamicColor = false) {
                PortfolioScreen()
            }
        }
    }
}
