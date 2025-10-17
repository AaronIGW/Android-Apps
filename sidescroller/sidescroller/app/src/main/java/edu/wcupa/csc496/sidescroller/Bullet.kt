package edu.wcupa.csc496.sidescroller

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.Bitmap as BMP

class Bullet(context: Context, startX: Int, startY: Int, rx: Float, ry: Float) {
    val bitmap: Bitmap
    var x: Int = startX
    var y: Int = startY
    private val width: Int
    private val height: Int

    init {
        val b = BitmapFactory.decodeResource(context.resources, R.drawable.bullet)
        width = (b.width * rx).toInt().coerceAtLeast(2)
        height = (b.height * ry).toInt().coerceAtLeast(2)
        bitmap = BMP.createScaledBitmap(b, width, height, true)
        y -= height / 2
    }

    fun getCollision(): Rect = Rect(x, y, x + width, y + height)
}
