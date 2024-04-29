package com.example.weatherapp.data.sharedPreference

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.weatherapp.data.models.LocationBulk
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Inject


const val placePreference = "placePreference"

@Module
@InstallIn(SingletonComponent::class)
class EncryptedSharedPreference @Inject constructor(@ApplicationContext appContext: Context) {

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

    private fun writeToSharedPrefs(key: String, value: Any?) {
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

    fun saveMyPreferredLocations(myObjects: List<LocationBulk>) {
        val gson = Gson()
        val json = gson.toJson(myObjects)
        writeToSharedPrefs(placePreference, json)
    }

    fun retrieveMyPreferredLocations(): List<LocationBulk> {
        val gson = Gson()
        val json = readFromSharedPrefs(placePreference, "")
        val type = object : TypeToken<List<LocationBulk>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}