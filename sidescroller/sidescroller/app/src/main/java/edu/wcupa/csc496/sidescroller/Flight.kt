package edu.wcupa.csc496.sidescroller

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import kotlin.math.min

class Flight(context: Context, screenY: Int, rx: Float, ry: Float) {
    val fly1: Bitmap
    val fly2: Bitmap
    val shoot1: Bitmap
    val shoot2: Bitmap
    val shoot3: Bitmap
    val shoot4: Bitmap
    val shoot5: Bitmap
    val dead: Bitmap
    var width: Int
    var height: Int
    var x: Int = (64 * rx).toInt()
    var y: Int = screenY / 2
    var isGoingUp: Boolean = false
    var toShoot: Int = 0
    private var wing = 0
    private var shootFrame = 0

    init {
        val f1 = BitmapFactory.decodeResource(context.resources, R.drawable.fly1)
        val f2 = BitmapFactory.decodeResource(context.resources, R.drawable.fly2)

        var w = (f1.width * rx).toInt().coerceAtLeast(1)
        var h = (f1.height * ry).toInt().coerceAtLeast(1)
        val maxH = (screenY * 0.14f).toInt().coerceAtLeast(1)
        if (h > maxH) {
            val s = maxH.toFloat() / h
            h = maxH
            w = (w * s).toInt().coerceAtLeast(1)
        }

        fly1 = Bitmap.createScaledBitmap(f1, w, h, true)
        fly2 = Bitmap.createScaledBitmap(f2, w, h, true)
        width = w
        height = h

        val s1 = BitmapFactory.decodeResource(context.resources, R.drawable.shoot1)
        val s2 = BitmapFactory.decodeResource(context.resources, R.drawable.shoot2)
        val s3 = BitmapFactory.decodeResource(context.resources, R.drawable.shoot3)
        val s4 = BitmapFactory.decodeResource(context.resources, R.drawable.shoot4)
        val s5 = BitmapFactory.decodeResource(context.resources, R.drawable.shoot5)
        shoot1 = Bitmap.createScaledBitmap(s1, w, h, true)
        shoot2 = Bitmap.createScaledBitmap(s2, w, h, true)
        shoot3 = Bitmap.createScaledBitmap(s3, w, h, true)
        shoot4 = Bitmap.createScaledBitmap(s4, w, h, true)
        shoot5 = Bitmap.createScaledBitmap(s5, w, h, true)

        val d = BitmapFactory.decodeResource(context.resources, R.drawable.dead)
        dead = Bitmap.createScaledBitmap(d, w, h, true)
    }

    fun getBitmap(): Bitmap {
        if (toShoot > 0) {
            shootFrame = (shootFrame + 1) % 5
            return when (shootFrame) {
                0 -> shoot1
                1 -> shoot2
                2 -> shoot3
                3 -> shoot4
                else -> shoot5
            }
        }
        wing = 1 - wing
        return if (wing == 0) fly1 else fly2
    }

    fun getCollision(): Rect = Rect(x, y, x + width, y + height)
}
