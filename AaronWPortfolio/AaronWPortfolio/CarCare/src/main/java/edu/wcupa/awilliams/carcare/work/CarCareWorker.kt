package edu.wcupa.awilliams.carcare.work

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.ListenableWorker.Result
import edu.wcupa.awilliams.carcare.MainActivity
import edu.wcupa.awilliams.carcare.R

class CarCareWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val car = inputData.getString("car") ?: "Car"
        val task = inputData.getString("task") ?: "Task"
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pi = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(applicationContext, Notifications.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("$car — $task")
            .setContentText(applicationContext.getString(R.string.cc_due))
            .setContentIntent(pi)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(applicationContext).notify((System.currentTimeMillis() % Int.MAX_VALUE).toInt(), notification)
        }

        return Result.success()
    }
}
