package com.jshukla.forgetusnot

import android.annotation.SuppressLint

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val notif_id = intent.getIntExtra("notification_id", 0)
        val title = intent.getStringExtra("title")
        val desc = intent.getStringExtra("desc")
        val dataSource = ReminderDataSource(context)
        val builder = NotificationCompat.Builder(context, "Rems")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title.toString())
            .setContentText(desc.toString())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(notif_id, builder.build())
        }
        dataSource.open()
        val reminder = dataSource.getRowById(notif_id)!!
        dataSource.deleteReminder(reminder)
        dataSource.close()
    }
}