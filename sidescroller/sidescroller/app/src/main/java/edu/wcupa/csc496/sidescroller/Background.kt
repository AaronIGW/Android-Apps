package edu.wcupa.csc496.sidescroller

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Background(context: Context, screenX: Int, screenY: Int, startX: Int) {
    val bitmap: Bitmap
    val width: Int
    val height: Int
    var x: Int = startX
    var y: Int = 0
    init {
        val bmp = BitmapFactory.decodeResource(context.resources, R.drawable.background)
        val scaled = Bitmap.createScaledBitmap(bmp, screenX, screenY, true)
        bitmap = scaled
        width = scaled.width
        height = scaled.height
    }
}
