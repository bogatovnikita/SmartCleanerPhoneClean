package com.softcleean.fastcleaner.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toBitmap
import com.softcleean.fastcleaner.App
import com.softcleean.fastcleaner.R
import com.yandex.metrica.YandexMetrica

private val CHANNEL_ID = App::getPackageName.toString()
private const val NOTIFICATION_ID = 1

fun showNotification(context: Context, title: String, message: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = CHANNEL_ID
        val descriptionText = CHANNEL_ID
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setContentTitle(context.packageName)
        .setSmallIcon(R.drawable.img)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .setDefaults(Notification.DEFAULT_ALL)

    NotificationManagerCompat.from(context).apply {
        notify(NOTIFICATION_ID, builder.build())

    }
}