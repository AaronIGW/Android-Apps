package edu.wcupa.csc496.sidescroller

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {
    private lateinit var gameView: GameView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val metrics = resources.displayMetrics
        gameView = GameView(this, metrics.widthPixels, metrics.heightPixels)
        setContentView(gameView)
    }
    override fun onPause() {
        super.onPause()
        gameView.pause()
    }
    override fun onResume() {
        super.onResume()
        gameView.resume()
    }
}
