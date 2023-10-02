package com.jshukla.forgetusnot

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jshukla.forgetusnot.databinding.ActivityInfoEditDeleteBinding
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import android.app.AlertDialog
import android.content.Intent
import java.time.format.DateTimeFormatter


class InfoEditDelete() : AppCompatActivity() {
    private lateinit var binding: ActivityInfoEditDeleteBinding
    private val dataSource = ReminderDataSource(this)
    private var reminder: Reminder? = null
    private var id = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.getIntExtra("id", -1)
        binding = ActivityInfoEditDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (id != -1) {
            dataSource.open()
            reminder = dataSource.getRowById(id)
            dataSource.close()
        }

        binding.ReminderTitle.text = reminder!!.title
        binding.ReminderDescription.text = reminder!!.description
        val timestamp = reminder!!.timestamp
        val instant = Instant.ofEpochMilli(timestamp)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter1 = DateTimeFormatter.ofPattern("dd MMM yy")
        val dateString = localDateTime.format(formatter1)
        val formatter2 = DateTimeFormatter.ofPattern("hh:mm a")
        val timeString = localDateTime.format(formatter2)
        binding.ReminderTime.text = "Date: $dateString \nTime: $timeString"

        binding.DeleteButton.setOnClickListener {
            showWarningDialog()

        }
        binding.EditButton.setOnClickListener {
            val intent = Intent(this, EditReminder::class.java)
            intent.putExtra("id", reminder!!.id)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (id != -1) {
            dataSource.open()
            reminder = dataSource.getRowById(id)
            dataSource.close()
        }

        binding.ReminderTitle.text = reminder!!.title
        binding.ReminderDescription.text = reminder!!.description
        val timestamp = reminder!!.timestamp
        val instant = Instant.ofEpochMilli(timestamp)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter1 = DateTimeFormatter.ofPattern("dd MMM yy")
        val dateString = localDateTime.format(formatter1)
        val formatter2 = DateTimeFormatter.ofPattern("hh:mm a")
        val timeString = localDateTime.format(formatter2)
        binding.ReminderTime.text = "Date: $dateString \nTime: $timeString"
    }


    private fun showWarningDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)

        // Set the title and message
        alertDialogBuilder.setTitle("Are You Sure?")
        alertDialogBuilder.setMessage("You are about to delete this reminder, this action is irreversible, do you want to proceed")

        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert)

        // Set a positive button
        alertDialogBuilder.setPositiveButton("Delete") { dialog, _ ->
            dataSource.open()
            dataSource.deleteReminder(reminder!!)
            dataSource.close()
            val notificationManager = MyNotificationManager(this)
            notificationManager.deleteNotification(reminder!!.id)
            dialog.dismiss()
            finish()
        }

        // Create and show the dialog
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}