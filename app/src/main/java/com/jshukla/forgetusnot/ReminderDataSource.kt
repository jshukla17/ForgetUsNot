package com.jshukla.forgetusnot


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class ReminderDataSource(context: Context) {

    private val dbHelper: ReminderDatabaseHelper = ReminderDatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    fun open() {
        database = dbHelper.writableDatabase
    }

    fun close() {
        dbHelper.close()
    }

    fun createReminder(id: Int, title: String, description: String, timestamp: Long): Long {
        val values = ContentValues().apply {
            put(ReminderDatabaseHelper.COLUMN_ID, id)
            put(ReminderDatabaseHelper.COLUMN_TITLE, title)
            put(ReminderDatabaseHelper.COLUMN_DESCRIPTION, description)
            put(ReminderDatabaseHelper.COLUMN_TIMESTAMP, timestamp)
        }
        return database.insert(ReminderDatabaseHelper.TABLE_NAME, null, values)
    }

    fun getAllReminders(): List<Reminder> {
        val reminders = mutableListOf<Reminder>()
        val cursor: Cursor? = database.query(
            ReminderDatabaseHelper.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "timestamp desc"
        )
        cursor?.let {
            val idIndex = cursor.getColumnIndex(ReminderDatabaseHelper.COLUMN_ID)
            val titleIndex = cursor.getColumnIndex(ReminderDatabaseHelper.COLUMN_TITLE)
            val descriptionIndex = cursor.getColumnIndex(ReminderDatabaseHelper.COLUMN_DESCRIPTION)
            val timestampIndex = cursor.getColumnIndex(ReminderDatabaseHelper.COLUMN_TIMESTAMP)

            while (cursor.moveToNext()) {
                val id = if (idIndex >= 0) cursor.getInt(idIndex) else -1
                val title = if (titleIndex >= 0) cursor.getString(titleIndex) else ""
                val description = if (descriptionIndex >= 0) cursor.getString(descriptionIndex) else ""
                val timestamp = if (timestampIndex >= 0) cursor.getLong(timestampIndex) else -1

                reminders.add(Reminder(id, title, description, timestamp))
            }
            cursor.close()
        }
        return reminders
    }

    fun getHighestId() : Reminder?{
        var reminder: Reminder? = null
        val cursor: Cursor? = database.query(
            ReminderDatabaseHelper.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "id desc"
        )
        cursor?.let {
            val idIndex = cursor.getColumnIndex(ReminderDatabaseHelper.COLUMN_ID)
            val titleIndex = cursor.getColumnIndex(ReminderDatabaseHelper.COLUMN_TITLE)
            val descriptionIndex = cursor.getColumnIndex(ReminderDatabaseHelper.COLUMN_DESCRIPTION)
            val timestampIndex = cursor.getColumnIndex(ReminderDatabaseHelper.COLUMN_TIMESTAMP)

            if(cursor.moveToNext()) {
                val id = if (idIndex >= 0) cursor.getInt(idIndex) else -1
                val title = if (titleIndex >= 0) cursor.getString(titleIndex) else ""
                val description =
                    if (descriptionIndex >= 0) cursor.getString(descriptionIndex) else ""
                val timestamp = if (timestampIndex >= 0) cursor.getLong(timestampIndex) else -1
                reminder = Reminder(id, title, description, timestamp)
            }
            cursor.close()
        }
        return reminder
    }

    fun updateReminder(id: Int, title: String, description: String, timestamp: Long) {
        val values = ContentValues().apply {
            put(ReminderDatabaseHelper.COLUMN_ID, id)
            put(ReminderDatabaseHelper.COLUMN_TITLE, title)
            put(ReminderDatabaseHelper.COLUMN_DESCRIPTION, description)
            put(ReminderDatabaseHelper.COLUMN_TIMESTAMP, timestamp)
        }
        val whereClause = "id = ?"

        val whereArgs = arrayOf(id.toString())

        database.update(ReminderDatabaseHelper.TABLE_NAME, values, whereClause, whereArgs)

    }


    @SuppressLint("Range")
    fun getRowById(id: Int): Reminder? {

        val selectQuery = "SELECT * FROM reminders WHERE id = ?"
        val selectionArgs = arrayOf(id.toString())

        var row: Reminder? = null
        val cursor = database.rawQuery(selectQuery, selectionArgs)
        if (cursor.moveToFirst()) {
            val title = cursor.getString(cursor.getColumnIndex("title"))
            val description = cursor.getString(cursor.getColumnIndex("description"))
            val timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"))

            // Create an instance of YourRowModel using the retrieved data
            row = Reminder(id, title, description, timestamp)
        }
        cursor.close()

        return row
    }


    fun deleteReminder(reminder: Reminder) {
        val selection = "${ReminderDatabaseHelper.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(reminder.id.toString())
        database.delete(ReminderDatabaseHelper.TABLE_NAME, selection, selectionArgs)
    }
}
