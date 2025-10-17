package edu.wcupa.csc496.sidescroller

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import kotlin.math.min
import kotlin.random.Random

class Bird(context: Context, screenX: Int, screenY: Int, rx: Float, ry: Float) {
    private val bird1: Bitmap
    private val bird2: Bitmap
    private val bird3: Bitmap
    private val bird4: Bitmap
    var width: Int
    var height: Int
    var x: Int
    var y: Int
    var speed: Int
    var wasShot: Boolean = false
    private var frame = 0
    private val random = Random(System.currentTimeMillis())

    init {
        val b1 = BitmapFactory.decodeResource(context.resources, R.drawable.bird1)
        val b2 = BitmapFactory.decodeResource(context.resources, R.drawable.bird2)
        val b3 = BitmapFactory.decodeResource(context.resources, R.drawable.bird3)
        val b4 = BitmapFactory.decodeResource(context.resources, R.drawable.bird4)

        var w = (b1.width * rx).toInt().coerceAtLeast(1)
        var h = (b1.height * ry).toInt().coerceAtLeast(1)
        val maxH = (screenY * 0.10f).toInt().coerceAtLeast(1)
        if (h > maxH) {
            val s = maxH.toFloat() / h
            h = maxH
            w = (w * s).toInt().coerceAtLeast(1)
        }

        bird1 = Bitmap.createScaledBitmap(b1, w, h, true)
        bird2 = Bitmap.createScaledBitmap(b2, w, h, true)
        bird3 = Bitmap.createScaledBitmap(b3, w, h, true)
        bird4 = Bitmap.createScaledBitmap(b4, w, h, true)

        width = w
        height = h
        x = screenX
        val maxY = (screenY - height).coerceAtLeast(0)
        y = if (maxY == 0) 0 else random.nextInt(0, maxY)
        speed = (random.nextInt(8, 14) * rx).toInt().coerceAtLeast(1)
    }

    fun getBitmap(): Bitmap {
        frame = (frame + 1) % 4
        return when (frame) {
            0 -> bird1
            1 -> bird2
            2 -> bird3
            else -> bird4
        }
    }

    fun getCollision(): Rect = Rect(x, y, x + width, y + height)
}
