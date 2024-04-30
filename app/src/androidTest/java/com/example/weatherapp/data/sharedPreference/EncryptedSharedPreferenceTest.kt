package com.example.weatherapp.data.sharedPreference

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.example.weatherapp.data.models.LocationBulk
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class EncryptedSharedPreferenceTest {

    private lateinit var encryptedSharedPreference: EncryptedSharedPreference
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor

    @Before
    fun setUp() {
        // Mocking the EncryptedSharedPreferences and its editor
        sharedPreferences = mock(SharedPreferences::class.java)
        sharedPreferencesEditor = mock(SharedPreferences.Editor::class.java)
        `when`(sharedPreferences.edit()).thenReturn(sharedPreferencesEditor)
        `when`(sharedPreferencesEditor.putString(Mockito.anyString(), Mockito.anyString())).thenReturn(sharedPreferencesEditor)
        `when`(sharedPreferencesEditor.putInt(Mockito.anyString(), Mockito.anyInt())).thenReturn(sharedPreferencesEditor)
        `when`(sharedPreferencesEditor.apply()).thenReturn(Unit)

        // Assuming mainKeyAlias and encryptedSharedPreferences are accessible to be mocked or use Reflection to inject
        encryptedSharedPreference = EncryptedSharedPreference(context)
        // Reflection or constructor modification might be necessary to inject the mocked sharedPreferences
    }

    @Test
    fun saveMyPreferredLocations_writes_to_SharedPreferences() {
        val locations = listOf(LocationBulk("Location1"), LocationBulk("Location2"))
        encryptedSharedPreference.saveMyPreferredLocations(locations)

        Mockito.verify(sharedPreferencesEditor).putString(Mockito.anyString(), Mockito.anyString())
        Mockito.verify(sharedPreferencesEditor).apply()
    }

    @Test
    fun retrieveMyPreferredLocations_reads_from_SharedPreferences() {
        val locations = listOf(LocationBulk("Location1"), LocationBulk("Location2"))
        val gson = Gson()
        val json = gson.toJson(locations)

        `when`(sharedPreferences.getString(Mockito.anyString(), Mockito.anyString())).thenReturn(json)

        val retrievedLocations = encryptedSharedPreference.retrieveMyPreferredLocations()
        assertNotNull(retrievedLocations)
        assertEquals(locations.size, retrievedLocations.size)
        assertEquals(locations[0].custom_id, retrievedLocations[0].custom_id)
    }
}