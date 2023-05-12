package com.smart.cleaner.phone.clean.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class AlarmManagerReceiver(private val context: Context) {

    private val alarmManager by lazy {
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    private val alarmIntent by lazy {
        Intent(context, NotificationReceiver::class.java)
    }

    private val pendingIntent by lazy {
        PendingIntent.getBroadcast(context, 0, alarmIntent, 0)
    }

    fun startAlarmManager() {
        val triggerAtMillis = System.currentTimeMillis() + INTERVAL_MILLIS

        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }

    fun cancelAlarmManager() {
        alarmManager.cancel(pendingIntent)
    }


    companion object {
        const val INTERVAL_MILLIS = 2 * 60 * 60 * 1000L
    }
}