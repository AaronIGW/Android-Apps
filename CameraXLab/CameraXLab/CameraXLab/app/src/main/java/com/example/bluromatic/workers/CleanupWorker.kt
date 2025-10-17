package com.example.bluromatic.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.bluromatic.DELAY_TIME_MILLIS
import com.example.bluromatic.OUTPUT_PATH
import com.example.bluromatic.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File

class CleanupWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            makeStatusNotification(
                applicationContext.getString(R.string.cleaning_up_files),
                applicationContext
            )
            delay(DELAY_TIME_MILLIS)
            val outputDir = File(applicationContext.filesDir, OUTPUT_PATH)
            if (outputDir.exists()) {
                outputDir.listFiles()?.forEach { if (it.isFile) it.delete() }
            }
            Result.success()
        } catch (t: Throwable) {
            Result.failure()
        }
    }
}