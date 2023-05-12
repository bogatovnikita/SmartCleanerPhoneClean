package com.smart.cleaner.phone.clean.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class AlarmManagerReceiver(context: Context) {

    init {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, NotificationReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)

        alarmManager.cancel(pendingIntent)

        val triggerAtMillis = System.currentTimeMillis() + INTERVAL_MILLIS
        val updatedPendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)

        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            updatedPendingIntent
        )
    }

    companion object{
        const val INTERVAL_MILLIS = 2 * 60 * 60 * 1000L
    }
}