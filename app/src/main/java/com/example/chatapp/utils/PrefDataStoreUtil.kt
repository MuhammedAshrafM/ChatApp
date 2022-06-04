package com.example.chatapp.utils

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


class PrefDataStoreUtil @Inject constructor(@ApplicationContext val context: Context) {

    companion object{
        private const val APP_PREFERENCES = "chatApp-preferences"
        private const val PREF_FIRST_TIME = "chatApp-prefs-first-time"
        private const val PREF_LOGGED = "chatApp-prefs-logged"
    }

    private val FIRST_TIME_KEY = booleanPreferencesKey(PREF_FIRST_TIME)
    private val LOGGED_KEY = booleanPreferencesKey(PREF_LOGGED)

    private val Context.appPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
        name = APP_PREFERENCES
    )

    suspend fun enableFirstTime(firstTime: Boolean): Boolean{
        val result = context.appPreferencesDataStore.edit { preferences ->
            preferences[FIRST_TIME_KEY] = firstTime
        }
        return result.toPreferences().get(FIRST_TIME_KEY) ?: true
    }

    suspend fun setUserAsLogged(isLogged: Boolean) {
        context.appPreferencesDataStore.edit { preferences ->
            preferences[LOGGED_KEY] = isLogged
        }
    }

    fun isAppFirstTime(): Flow<Boolean> = context.appPreferencesDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[FIRST_TIME_KEY] ?: true
        }

    fun isUserLogged(): Flow<Boolean> = context.appPreferencesDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[LOGGED_KEY] ?: true
        }
}