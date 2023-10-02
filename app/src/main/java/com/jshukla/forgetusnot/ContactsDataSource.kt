package com.jshukla.forgetusnot

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class ContactsDataSource(context: Context) {
    private val dbHelper: ReminderDatabaseHelper = ReminderDatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    fun open() {
        database = dbHelper.writableDatabase
    }

    fun close() {
        dbHelper.close()
    }

    fun addContact(id: String, name: String, pictureUri: String, frequency: Int): Long{
        val values = ContentValues().apply {
            put("id", id)
            put("name", name)
            put("pictureURI", pictureUri)
            put("frequency", frequency)
        }
        return database.insert("loved_ones", null, values)
    }

    fun deleteContact(id: String) {
        val selection = "id = ?"
        val selectionArgs = arrayOf(id)
        database.delete(ReminderDatabaseHelper.TABLE_NAME, selection, selectionArgs)
    }
}