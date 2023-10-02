package com.jshukla.forgetusnot


data class Reminder(
    val id : Int,
    val title : String,
    val description: String,
    val timestamp: Long
)