package com.smart.cleaner.phone.clean.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.smart.cleaner.phone.clean.notification.NotificationUtils

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        NotificationUtils(context)
    }
}
