package com.jshukla.forgetusnot


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.jshukla.forgetusnot.databinding.ActivityEditReminderBinding
import java.util.Calendar



class EditReminder : AppCompatActivity() {

    private lateinit var binding : ActivityEditReminderBinding
    private val dataSource = ReminderDataSource(this)
    private val calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.getIntExtra("id", -1)
        binding = ActivityEditReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataSource.open()
        val reminder = dataSource.getRowById(id)
        dataSource.close()
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) -> {}
            else -> {
                if(Build.VERSION.SDK_INT>=34){
                    requestPermissions(
                        arrayOf( android.Manifest.permission.POST_NOTIFICATIONS), 123)
                }
            }
        }

        val title = binding.titleET
        val desc = binding.descriptionET

        title.setText(reminder!!.title)
        desc.setText(reminder!!.description)


        binding.selectTimeButton.setOnClickListener { TPHandler() }
        binding.selectDateButton.setOnClickListener { DPHandler() }

        binding.saveBtn.setOnClickListener {
            dataSource.open()
            dataSource.updateReminder(id, title.text.toString(), desc.text.toString(), calendar.timeInMillis)
            println("id")
            dataSource.close()
            println("Button clicked")
            MyNotificationManager(this).updateNotification(id, title.text.toString(), desc.text.toString(), calendar)
            Toast.makeText(this, "Reminder Changed", Toast.LENGTH_LONG).show()
            finish()
        }

    }




    private fun DPHandler() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                calendar.apply {
                    set(year, monthOfYear, dayOfMonth)
                }
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
    private fun TPHandler() {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }
}