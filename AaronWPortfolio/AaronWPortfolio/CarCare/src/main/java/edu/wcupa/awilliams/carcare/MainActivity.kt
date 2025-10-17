package edu.wcupa.awilliams.carcare

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import edu.wcupa.awilliams.carcare.ui.CarCareApp
import edu.wcupa.awilliams.carcare.ui.theme.CarCareTheme
import edu.wcupa.awilliams.carcare.work.Notifications

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Notifications.createChannel(this)
        if (Build.VERSION.SDK_INT >= 33) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
        setContent {
            CarCareTheme {
                Column(Modifier.fillMaxSize()) {
                    Button(onClick = { finish() }) {
                        Text("Return Home")
                    }
                    CarCareApp()
                }
            }
        }
    }
}
