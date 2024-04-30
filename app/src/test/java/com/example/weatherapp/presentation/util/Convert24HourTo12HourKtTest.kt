package com.example.weatherapp.presentation.util

import org.junit.Test
import kotlin.test.assertEquals

class TimeConversionTest {

    @Test
    fun testConvert24HourTo12Hour_Midnight() {
        val result = convert24HourTo12Hour("00:00")
        assertEquals("12:00 AM", result)
    }

    @Test
    fun testConvert24HourTo12Hour_Noon() {
        val result = convert24HourTo12Hour("12:00")
        assertEquals("12:00 PM", result)
    }

    @Test
    fun testConvert24HourTo12Hour_Evening() {
        val result = convert24HourTo12Hour("23:59")
        assertEquals("11:59 PM", result)
    }

    @Test
    fun testConvert24HourTo12Hour_EarlyMorning() {
        val result = convert24HourTo12Hour("01:00")
        assertEquals("01:00 AM", result)
    }

    @Test
    fun testConvert24HourTo12Hour_InvalidInput() {
        val result = convert24HourTo12Hour("25:00")
        assertEquals("", result)
    }
}
