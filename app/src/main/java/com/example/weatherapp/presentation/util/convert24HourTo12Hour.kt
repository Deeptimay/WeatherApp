package com.example.weatherapp.presentation.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun convert24HourTo12Hour(time24Hour: String): String {
    val sdf24Hour = SimpleDateFormat("HH:mm", Locale.getDefault())
    val sdf12Hour = SimpleDateFormat("hh:mm a", Locale.getDefault())

    val time: Date? = sdf24Hour.parse(time24Hour)
    return time?.let { sdf12Hour.format(it) } ?: ""
}

fun isDaytime(time: String): Boolean {
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val date: Date = dateFormat.parse(time) ?: return false
    val hour = date.hours
    return hour in 6..17 // Assuming daytime is from 6 AM to 5 PM
}