package com.example.weatherapp.presentation.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class DebounceTest {

    private lateinit var scope: TestCoroutineScope

    @Before
    fun setUp() {
        scope = TestCoroutineScope()
    }

    @After
    fun tearDown() {
        scope.cleanupTestCoroutines()
    }

    @Test
    fun `debounce should only invoke last call after rapid succession`() = scope.runBlockingTest {
        var latestValue: Int = 0
        val debounce = debounce<Int>(
            waitMs = 100L,
            scope = scope,
            destinationFunction = { latestValue = it }
        )

        debounce(1)
        debounce(2)
        debounce(3)  // Only this should be invoked after debounce timeout

        advanceUntilIdle()  // Instead of advanceTimeBy
        assertEquals(3, latestValue, "Only the last invoked value should be processed")
    }

    @Test
    fun `debounce respects the wait time`() = scope.runBlockingTest {
        val values = mutableListOf<Int>()
        val debounce = debounce<Int>(
            waitMs = 300L,
            scope = scope,
            destinationFunction = { values.add(it) }
        )

        debounce(10)  // This should be invoked after 300 ms

        advanceUntilIdle()
        assertEquals(0, values.size, "No values should be processed before 300 ms")

        advanceUntilIdle()
        assertEquals(listOf(10), values, "Value 10 should be processed after 300 ms")
    }
}

