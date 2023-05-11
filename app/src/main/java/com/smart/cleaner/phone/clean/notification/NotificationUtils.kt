package com.smart.cleaner.phone.clean.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.smart.cleaner.phone.clean.R

class NotificationUtils(private val context: Context) {

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val channelId = "my_channel_id"
    private val channelName = "My Channel"
    private val channelDescription = "Notification Channel"

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = channelDescription
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification() {
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_boost_done)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(RemoteViews(context.packageName, R.layout.item_notification))
            .setAutoCancel(true)
            .build()

        notificationManager.notify(123, notification)
    }
}
