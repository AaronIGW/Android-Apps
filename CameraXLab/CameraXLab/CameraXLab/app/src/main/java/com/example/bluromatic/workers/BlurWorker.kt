package com.example.bluromatic.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.bluromatic.DELAY_TIME_MILLIS
import com.example.bluromatic.KEY_BLUR_LEVEL
import com.example.bluromatic.KEY_IMAGE_URI
import com.example.bluromatic.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

private const val TAG = "BlurWorker"

class BlurWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        makeStatusNotification(
            applicationContext.getString(R.string.blurring_image),
            applicationContext
        )
        try {
            delay(DELAY_TIME_MILLIS)
            val resourceUri = inputData.getString(KEY_IMAGE_URI)
            val blurLevel = inputData.getInt(KEY_BLUR_LEVEL, 1)
            val bitmap = if (!resourceUri.isNullOrEmpty()) {
                applicationContext.contentResolver.openInputStream(Uri.parse(resourceUri))
                    .use { BitmapFactory.decodeStream(it) }
            } else {
                BitmapFactory.decodeResource(
                    applicationContext.resources,
                    R.drawable.android_cupcake
                )
            }
            val output = blurBitmap(bitmap, blurLevel)
            val outputUri = writeBitmapToFile(applicationContext, output)
            makeStatusNotification("Output is $outputUri", applicationContext)
            Result.success(workDataOf(KEY_IMAGE_URI to outputUri.toString()))
        } catch (t: Throwable) {
            Log.e(TAG, applicationContext.getString(R.string.error_applying_blur), t)
            Result.failure()
        }
    }
}