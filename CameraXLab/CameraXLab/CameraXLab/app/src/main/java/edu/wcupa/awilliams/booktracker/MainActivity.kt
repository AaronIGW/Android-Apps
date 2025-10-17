package edu.wcupa.awilliams.booktracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import edu.wcupa.awilliams.booktracker.ui.BookTrackerApp
import edu.wcupa.awilliams.booktracker.ui.theme.BookTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookTrackerTheme {
                Column(Modifier.fillMaxSize()) {
                    Button(onClick = { finish() }) {
                        Text("Return Home")
                    }
                    BookTrackerApp()
                }
            }
        }
    }
}
