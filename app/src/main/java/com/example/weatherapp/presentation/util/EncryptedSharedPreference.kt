package com.example.weatherapp.presentation.util

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.nio.charset.StandardCharsets
import javax.inject.Singleton

@Singleton
class EncryptedSharedPreference(@ApplicationContext appContext: Context) {

    private val mainKeyAlias by lazy {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        MasterKeys.getOrCreate(keyGenParameterSpec)
    }

    private val sharedPreferences by lazy {
        val sharedPrefsFile = "sharedPrefs"

        EncryptedSharedPreferences.create(
            sharedPrefsFile,
            mainKeyAlias,
            appContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun writeToSharedPrefs(key: String, value: Any?) {
        with(sharedPreferences.edit()) {
            when (value) {
                is String -> this.putString(key, value)
                is Int -> this.putInt(key, value)
                is Boolean -> this.putBoolean(key, value)
                is Float -> this.putFloat(key, value)
                is Long -> this.putLong(key, value)
                else -> throw UnsupportedOperationException("Not yet implemented")
            }
            apply()
        }
    }

    private inline fun <reified T : Any> readFromSharedPrefs(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> sharedPreferences.getString(key, defaultValue as? String) as T?
            Int::class -> sharedPreferences.getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> sharedPreferences.getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> sharedPreferences.getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> sharedPreferences.getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    private val encryptedFile by lazy {
        val baseDir = appContext.filesDir
        val fileToWrite = File(baseDir, "encrypted-file.txt")

        EncryptedFile.Builder(
            fileToWrite,
            appContext,
            mainKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
    }

    fun writeToEncryptedFile(content: String) {
        encryptedFile.openFileOutput().use {
            it.write(content.toByteArray(StandardCharsets.UTF_8))
            it.flush()
        }
    }

    fun readFromEncryptedFile(): String {
        val fileContent = ByteArray(32000)
        val numBytesRead: Int

        try {
            encryptedFile.openFileInput().use {
                numBytesRead = it.read(fileContent)
            }
        } catch (e: Exception) {
            return ""
        }
        return String(fileContent, 0, numBytesRead)
    }
}