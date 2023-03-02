package com.softcleean.fastcleaner.ui.splash

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.softcleean.fastcleaner.utils.showNotification

class FirebaseMessagingReceiver : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.e("!!!", "Notification received. Data = ${message.data}")
    }


}