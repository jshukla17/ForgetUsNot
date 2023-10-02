package com.jshukla.forgetusnot

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

class MyNotificationManager(private val context: Context){
    fun showNotification(not_id: Int, title: String, desc: String, calendar: Calendar) {
        var intent = Intent( context, NotificationReceiver::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        }

        intent.putExtra("notification_id", not_id)
        intent.putExtra("title", title)
        intent.putExtra("desc", desc)
        val timestamp = calendar.timeInMillis
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, not_id, intent, PendingIntent.FLAG_MUTABLE)


        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent)
    }

    fun updateNotification(not_id: Int, title: String, desc: String, calendar: Calendar) {

        deleteNotification(not_id)

        var intent = Intent( context, NotificationReceiver::class.java)

        intent.putExtra("notification_id", not_id)
        intent.putExtra("title", title)
        intent.putExtra("desc", desc)
        val timestamp = calendar.timeInMillis
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, not_id, intent, PendingIntent.FLAG_MUTABLE)


        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent)
    }

    fun deleteNotification(id: Int){
        val intent = Intent( context, NotificationReceiver::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_MUTABLE)
        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }
}
