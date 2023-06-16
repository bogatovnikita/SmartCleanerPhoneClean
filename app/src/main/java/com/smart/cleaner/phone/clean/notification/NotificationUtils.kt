package com.smart.cleaner.phone.clean.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.smart.cleaner.phone.clean.MainActivity
import com.smart.cleaner.phone.clean.R

class NotificationUtils(context: Context) {

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val channelId = "my_channel_id"
    private val channelName = "Push notification"
    private val channelDescription = "Notification Channel"

    init {
        initChanel()
        val appPendingIntent = makePendingIntent(context)
        val remoteView = initCustomLayout(context, appPendingIntent)
        val notification = buildNotification(context, remoteView)
        notificationManager.notify(123, notification)
    }

    private fun buildNotification(
        context: Context,
        remoteView: RemoteViews
    ): Notification {
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .setWhen(System.currentTimeMillis())
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(remoteView)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
    }

    private fun initCustomLayout(
        context: Context,
        appPendingIntent: PendingIntent?
    ): RemoteViews {
        val remoteView = RemoteViews(context.packageName, R.layout.item_notification)
        remoteView.setOnClickPendingIntent(R.id.btn_open_app, appPendingIntent)
        remoteView.setOnClickPendingIntent(R.id.notification_plate, appPendingIntent)
        return remoteView
    }

    private fun makePendingIntent(context: Context): PendingIntent? {
        return PendingIntent.getActivity(
            context,
            123,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun initChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = channelDescription
            notificationManager.createNotificationChannel(channel)
        }
    }
}
