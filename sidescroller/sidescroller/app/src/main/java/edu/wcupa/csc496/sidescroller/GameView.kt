package edu.wcupa.csc496.sidescroller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.media.AudioAttributes
import android.media.SoundPool
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.random.Random

class GameView(private val ctx: Context, private val screenX: Int, private val screenY: Int) : SurfaceView(ctx), Runnable {
    private var thread: Thread? = null
    private var isPlaying = false
    private val surfaceHolder: SurfaceHolder = holder
    private val paint = Paint()
    private val bg1: Background
    private val bg2: Background
    private val flight: Flight
    private val birds = mutableListOf<Bird>()
    private val bullets = mutableListOf<Bullet>()
    private var score = 0
    private var isGameOver = false
    private var didSaveHigh = false
    private val random = Random(System.currentTimeMillis())
    private val screenRatioX: Float
    private val screenRatioY: Float
    private val soundPool: SoundPool
    private val shootSound: Int
    private val prefs = ctx.getSharedPreferences("game", Context.MODE_PRIVATE)
    private val isMute: Boolean
    private val initialBirds = 1
    private val maxBirds = 6
    private val increaseIntervalMs = 6000L
    private var nextIncreaseAt = System.currentTimeMillis() + increaseIntervalMs

    init {
        screenRatioX = screenX / 1920f
        screenRatioY = screenY / 1080f
        bg1 = Background(ctx, screenX, screenY, 0)
        bg2 = Background(ctx, screenX, screenY, screenX)
        flight = Flight(ctx, screenY, screenRatioX, screenRatioY)
        repeat(initialBirds) { birds.add(Bird(ctx, screenX, screenY, screenRatioX, screenRatioY)) }
        paint.textSize = (screenY * 0.05f)
        paint.color = Color.WHITE
        val aa = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
        soundPool = SoundPool.Builder().setMaxStreams(1).setAudioAttributes(aa).build()
        shootSound = soundPool.load(ctx, R.raw.shoot, 1)
        isMute = prefs.getBoolean("isMute", false)
    }

    override fun run() {
        while (isPlaying) {
            update()
            drawFrame()
            sleep()
        }
    }

    private fun update() {
        if (System.currentTimeMillis() >= nextIncreaseAt && birds.size < maxBirds) {
            birds.add(Bird(ctx, screenX, screenY, screenRatioX, screenRatioY))
            nextIncreaseAt += increaseIntervalMs
        }

        val bgSpeed = (10f * screenRatioX).toInt().coerceAtLeast(1)
        bg1.x -= bgSpeed
        bg2.x -= bgSpeed
        if (bg1.x + bg1.width < 0) bg1.x = bg2.x + bg2.width
        if (bg2.x + bg2.width < 0) bg2.x = bg1.x + bg1.width

        val upSpeed = (30f * screenRatioY).toInt().coerceAtLeast(1)
        val downSpeed = (20f * screenRatioY).toInt().coerceAtLeast(1)

        if (isGameOver) return

        if (flight.isGoingUp) flight.y -= upSpeed else flight.y += downSpeed
        if (flight.y < 0) flight.y = 0
        if (flight.y + flight.height > screenY) flight.y = screenY - flight.height

        if (flight.toShoot > 0) {
            bullets.add(Bullet(ctx, flight.x + flight.width, flight.y + flight.height / 2, screenRatioX, screenRatioY))
            flight.toShoot--
            if (!isMute) soundPool.play(shootSound, 1f, 1f, 1, 0, 1f)
        }

        val bulletSpeed = (50f * screenRatioX).toInt().coerceAtLeast(2)
        val it = bullets.iterator()
        while (it.hasNext()) {
            val b = it.next()
            b.x += bulletSpeed
            if (b.x > screenX) it.remove()
        }

        for (bird in birds) {
            bird.x -= bird.speed
            if (bird.x + bird.width < 0) {
                bird.speed = (kotlin.random.Random.nextInt(8, 14) * screenRatioX).toInt().coerceAtLeast(1)
                bird.x = screenX
                val maxY = (screenY - bird.height).coerceAtLeast(0)
                bird.y = if (maxY == 0) 0 else random.nextInt(0, maxY)
                bird.wasShot = false
            }
        }

        val bit = bullets.iterator()
        while (bit.hasNext()) {
            val b = bit.next()
            for (bird in birds) {
                if (Rect.intersects(b.getCollision(), bird.getCollision())) {
                    score++
                    bird.x = screenX
                    val maxY = (screenY - bird.height).coerceAtLeast(0)
                    bird.y = if (maxY == 0) 0 else random.nextInt(0, maxY)
                    bird.wasShot = true
                    bit.remove()
                    break
                }
            }
        }

        for (bird in birds) {
            if (Rect.intersects(bird.getCollision(), flight.getCollision())) {
                isGameOver = true
                return
            }
        }
    }

    private fun drawFrame() {
        if (!surfaceHolder.surface.isValid) return
        val canvas: Canvas = surfaceHolder.lockCanvas()
        canvas.drawBitmap(bg1.bitmap, bg1.x.toFloat(), bg1.y.toFloat(), paint)
        canvas.drawBitmap(bg2.bitmap, bg2.x.toFloat(), bg2.y.toFloat(), paint)
        for (bird in birds) {
            canvas.drawBitmap(bird.getBitmap(), bird.x.toFloat(), bird.y.toFloat(), paint)
        }
        val sprite = if (isGameOver) flight.dead else flight.getBitmap()
        canvas.drawBitmap(sprite, flight.x.toFloat(), flight.y.toFloat(), paint)
        for (b in bullets) {
            canvas.drawBitmap(b.bitmap, b.x.toFloat(), b.y.toFloat(), paint)
        }
        canvas.drawText("Score: $score", 32f, 80f, paint)

        if (isGameOver) {
            if (!didSaveHigh) {
                saveHighScore()
                didSaveHigh = true
            }
            val overlay = Paint()
            overlay.color = Color.argb(180, 0, 0, 0)
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), overlay)
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = (screenY * 0.10f)
            canvas.drawText("Game Over", width / 2f, height / 2f - (screenY * 0.06f), paint)
            paint.textSize = (screenY * 0.05f)
            canvas.drawText("Score: $score", width / 2f, height / 2f, paint)
            paint.textSize = (screenY * 0.035f)
            canvas.drawText("Tap anywhere to return to menu", width / 2f, height / 2f + (screenY * 0.08f), paint)
            paint.textAlign = Paint.Align.LEFT
        }

        surfaceHolder.unlockCanvasAndPost(canvas)
    }

    private fun sleep() {
        try { Thread.sleep(17) } catch (_: Exception) {}
    }

    fun pause() {
        isPlaying = false
        try { thread?.join() } catch (_: Exception) {}
    }

    fun resume() {
        isPlaying = true
        thread = Thread(this)
        thread?.start()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (isGameOver) {
                val a = ctx as Activity
                a.runOnUiThread {
                    a.startActivity(Intent(ctx, MainActivity::class.java))
                    a.finish()
                }
                return true
            }
            if (event.x < screenX / 2f) {
                flight.isGoingUp = true
            } else {
                flight.toShoot++
            }
        }
        if (event.action == MotionEvent.ACTION_UP) {
            flight.isGoingUp = false
        }
        return true
    }

    private fun saveHighScore() {
        val high = prefs.getInt("highscore", 0)
        if (score > high) {
            prefs.edit().putInt("highscore", score).apply()
        }
    }
}
