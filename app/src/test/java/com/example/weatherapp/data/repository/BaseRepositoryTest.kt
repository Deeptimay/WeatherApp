package com.example.weatherapp.data.repository

import com.example.weatherapp.domain.util.ErrorTypes
import com.example.weatherapp.domain.util.NetworkResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okio.IOException
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import retrofit2.Response
import java.net.SocketTimeoutException

@ExperimentalCoroutinesApi
class BaseRepositoryTest {

    private lateinit var baseRepository: BaseRepository

    @Before
    fun setUp() {
        baseRepository = BaseRepository()
    }

    @Test
    fun `performApiCall returns ApiSuccess on successful API call`() = runBlocking {
        val response = mockk<Response<Int>>()
        coEvery { response.isSuccessful } returns true
        coEvery { response.body() } returns 123
        coEvery { response.code() } returns 200

        val result = baseRepository.performApiCall { response }

        assertTrue(result is NetworkResult.ApiSuccess && result.data == 123)
    }

    @Test
    fun `performApiCall returns ApiError on unsuccessful API response`() = runBlocking {
        val response = mockk<Response<Int>>()
        coEvery { response.isSuccessful } returns false
        coEvery { response.code() } returns 400
        coEvery { response.errorBody() } returns mockk(relaxed = true)
        coEvery { response.errorBody().toString() } returns "Bad Request"

        val result = baseRepository.performApiCall { response }

        assertTrue(result is NetworkResult.ApiError && result.errorData is ErrorTypes.ServerError)
    }

    @Test
    fun `performApiCall handles IOException`() = runBlocking {
        val ioException = IOException("Failed to connect")

        val result = baseRepository.performApiCall<Int> { throw ioException }

        assertTrue(result is NetworkResult.ApiError && result.errorData is ErrorTypes.IOExceptionError)
    }

    @Test
    fun `performApiCall handles SocketTimeoutException`() = runBlocking {
        val timeoutException = SocketTimeoutException("Connection timed out")

        val result = baseRepository.performApiCall<Int> { throw timeoutException }

        assertTrue(result is NetworkResult.ApiError && result.errorData is ErrorTypes.TimeoutError)
    }

    @Test
    fun `performApiCall handles general Exception`() = runBlocking {
        val exception = Exception("General exception")

        val result = baseRepository.performApiCall<Int> { throw exception }

        assertTrue(result is NetworkResult.ApiError && result.errorData is ErrorTypes.ExceptionError)
    }
}
