package edu.wcupa.csc496.sidescroller

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val prefs = getSharedPreferences("game", MODE_PRIVATE)
        val high = prefs.getInt("highscore", 0)
        val mute = prefs.getBoolean("isMute", false)
        val tvHigh = findViewById<TextView>(R.id.tvHighScore)
        val btnMute = findViewById<Button>(R.id.btnMute)
        val btnPlay = findViewById<Button>(R.id.btnPlay)
        tvHigh.text = "High score: $high"
        btnMute.text = if (mute) "Mute: On" else "Mute: Off"
        btnMute.setOnClickListener {
            val m = !prefs.getBoolean("isMute", false)
            prefs.edit().putBoolean("isMute", m).apply()
            btnMute.text = if (m) "Mute: On" else "Mute: Off"
        }
        btnPlay.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }
    }
}
