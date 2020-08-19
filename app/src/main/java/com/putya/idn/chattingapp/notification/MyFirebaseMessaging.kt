package com.putya.idn.chattingapp.notification

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.MODE_PRIVATE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.core.Context
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.putya.idn.chattingapp.activity.MessageChatActivity

class MyFirebaseMessaging : FirebaseMessagingService() {

    @SuppressLint("ObsoleteSdkInt")
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        val sented = p0.data["sented"]
        val user = p0.data["user"]
        val sharedPref = getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        val currentOnlineUser = sharedPref.getString("currentUser", "none")
        val firebaseuser = FirebaseAuth.getInstance().currentUser
        if (firebaseuser != null && sented == firebaseuser.uid) {
            if (currentOnlineUser != user) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    sendOreoNotification(p0)
                } else {
                    sendNotification(p0)
                }
            }
        }
    }

    private fun sendNotification(p0: RemoteMessage) {
        val user = p0.data["user"]
        val icon = p0.data["icon"]
        val title = p0.data["title"]
        val body = p0.data["body"]
        val notification = p0.notification
        val not = user!!.replace("[\\D]".toRegex(), "").toInt()
        val intent = Intent(this, MessageChatActivity::class.java)

        val bundle = Bundle()
        bundle.putString("userId", user)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent =
            PendingIntent.getActivity(this, not, intent, PendingIntent.FLAG_ONE_SHOT)

        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this)
            .setSmallIcon(icon!!.toInt()).setContentTitle(title)
            .setContentText(body).setAutoCancel(true).setSound(defaultSound)
            .setContentIntent(pendingIntent)

        val notif = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var i = 0
        if (not > 0) {
            i = not
        }
        notif.notify(i, builder.build())
    }

    private fun sendOreoNotification(p0: RemoteMessage) {
        val user = p0.data["user"]
        val icon = p0.data["icon"]
        val title = p0.data["title"]
        val body = p0.data["body"]

        val notification = p0.notification
        val not = user!!.replace("[\\D]".toRegex(), "").toInt()
        val intent = Intent(this, MessageChatActivity::class.java)

        val bundle = Bundle()
        bundle.putString("userId", user)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent =
            PendingIntent.getActivity(this, not, intent, PendingIntent.FLAG_ONE_SHOT)

        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val oreoNotification = OreoNotification(this)

    }
}