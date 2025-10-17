package edu.wcupa.awilliams.carcare.work

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object Scheduler {
    fun scheduleMinutes(context: Context, car: String, task: String, minutes: Long) {
        enqueue(context, car, task, minutes, TimeUnit.MINUTES)
    }
    fun scheduleDays(context: Context, car: String, task: String, days: Long) {
        enqueue(context, car, task, days, TimeUnit.DAYS)
    }
    private fun enqueue(context: Context, car: String, task: String, duration: Long, unit: TimeUnit) {
        val input = Data.Builder()
            .putString("car", car)
            .putString("task", task)
            .build()
        val request = OneTimeWorkRequestBuilder<CarCareWorker>()
            .setInputData(input)
            .setInitialDelay(duration, unit)
            .build()
        WorkManager.getInstance(context).enqueue(request)
    }
}
