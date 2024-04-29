package com.example.weatherapp.presentation.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
fun <T> debounce(
    waitMs: Long = 300L,
    scope: CoroutineScope,
    destinationFunction: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    val context = newSingleThreadContext("Debounce")
    return { param: T ->
        scope.launch {
            withContext(context) {
                debounceJob?.cancel()
                debounceJob = scope.launch {
                    delay(waitMs)
                    destinationFunction(param)
                }
            }
        }
    }
}