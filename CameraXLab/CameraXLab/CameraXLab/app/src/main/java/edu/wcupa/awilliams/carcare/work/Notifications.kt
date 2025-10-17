package edu.wcupa.awilliams.carcare.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.content.getSystemService
import edu.wcupa.awilliams.carcare.R

object Notifications {
    const val CHANNEL_ID = "carcare_reminders"
    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= 26) {
            val mgr = context.getSystemService<NotificationManager>() ?: return
            val name = context.getString(R.string.cc_channel_name)
            val desc = context.getString(R.string.cc_channel_desc)
            val channel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = desc
            mgr.createNotificationChannel(channel)
        }
    }
}
