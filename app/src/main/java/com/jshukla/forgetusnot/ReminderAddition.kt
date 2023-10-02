package com.jshukla.forgetusnot


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jshukla.forgetusnot.databinding.ActivityReminderAdditionBinding
import java.util.Calendar



class ReminderAddition : AppCompatActivity() {

    private lateinit var binding : ActivityReminderAdditionBinding
    private val dataSource = ReminderDataSource(this)
    private val calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReminderAdditionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= 33){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                123
            )
        }

        val title = binding.titleET
        val desc = binding.descriptionET
        binding.selectTimeButton.setOnClickListener { TPHandler() }
        binding.selectDateButton.setOnClickListener { DPHandler() }

        binding.saveBtn.setOnClickListener {
            dataSource.open()
            val rems = dataSource.getHighestId()
            var id = 1
            if(rems != null){
                id = rems.id+1
            }
            dataSource.createReminder(id, title.text.toString(), desc.text.toString(), calendar.timeInMillis)
            println("id $id")
            dataSource.close()
            println("Button clicked")
            MyNotificationManager(this).showNotification(id, title.text.toString(), desc.text.toString(), calendar)
            Toast.makeText(this, "Reminder Added", Toast.LENGTH_LONG).show()
            finish()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 123) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with your logic
                // ...
            } else {
                // Permission denied, handle accordingly (e.g., show a message)
                // ...
            }
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