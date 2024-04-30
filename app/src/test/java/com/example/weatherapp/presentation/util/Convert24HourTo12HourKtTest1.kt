package com.example.weatherapp.presentation.util

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TimeUtilsTest {

    @Test
    fun testIsDaytime_EarlyMorning() {
        val result = isDaytime("05:59")
        assertFalse(result, "05:59 should not be considered daytime.")
    }

    @Test
    fun testIsDaytime_StartOfDaytime() {
        val result = isDaytime("06:00")
        assertTrue(result, "06:00 should be considered daytime.")
    }

    @Test
    fun testIsDaytime_MidDay() {
        val result = isDaytime("12:00")
        assertTrue(result, "12:00 should be considered daytime.")
    }

    @Test
    fun testIsDaytime_EndOfDaytime() {
        val result = isDaytime("17:00")
        assertTrue(result, "17:00 should be considered daytime.")
    }

    @Test
    fun testIsDaytime_Night() {
        val result = isDaytime("23:00")
        assertFalse(result, "23:00 should not be considered daytime.")
    }

    @Test
    fun testIsDaytime_InvalidInput() {
        val result = isDaytime("25:00")
        assertFalse(result, "25:00 is not a valid time and should not be considered daytime.")
    }
}
