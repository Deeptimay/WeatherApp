package com.example.weatherapp.domain.util

import okio.IOException
import retrofit2.HttpException
import java.net.SocketTimeoutException

sealed class ErrorTypes : Throwable() {
    data class ExceptionError(val exception: Throwable) : ErrorTypes()
    data class HttpExceptionError(val exception: HttpException) : ErrorTypes()
    data class IOExceptionError(val exception: IOException) : ErrorTypes()
    data class TimeoutError(val exception: SocketTimeoutException) : ErrorTypes()
    data class ServerError(val code: Int, val internalMessage: String) : ErrorTypes()
    data class CustomError(val code: Int, val internalMessage: String) : ErrorTypes()
}
