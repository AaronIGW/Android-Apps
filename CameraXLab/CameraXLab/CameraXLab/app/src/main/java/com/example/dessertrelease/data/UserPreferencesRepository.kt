package com.example.dessertrelease.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    val isLinearLayout: Flow<Boolean> = dataStore.data
        .safePrefs()
        .map { it[IS_LINEAR_LAYOUT] ?: true }

    val sortOrder: Flow<String> = dataStore.data
        .safePrefs()
        .map { it[SORT_ORDER] ?: "AZ" }

    val dynamicColor: Flow<Boolean> = dataStore.data
        .safePrefs()
        .map { it[DYNAMIC_COLOR] ?: true }

    val favorites: Flow<Set<String>> = dataStore.data
        .safePrefs()
        .map { it[FAVORITES] ?: emptySet() }

    suspend fun saveLayoutPreference(isLinearLayout: Boolean) {
        dataStore.edit { it[IS_LINEAR_LAYOUT] = isLinearLayout }
    }

    suspend fun saveSortOrder(order: String) {
        dataStore.edit { it[SORT_ORDER] = order }
    }

    suspend fun saveDynamicColor(enabled: Boolean) {
        dataStore.edit { it[DYNAMIC_COLOR] = enabled }
    }

    suspend fun toggleFavorite(name: String) {
        dataStore.edit { prefs ->
            val curr = prefs[FAVORITES] ?: emptySet()
            prefs[FAVORITES] = if (name in curr) curr - name else curr + name
        }
    }

    suspend fun resetDefaults() {
        dataStore.edit { prefs ->
            prefs[IS_LINEAR_LAYOUT] = true
            prefs[SORT_ORDER] = "AZ"
            prefs[DYNAMIC_COLOR] = true
            prefs[FAVORITES] = emptySet()
        }
    }

    private fun Flow<Preferences>.safePrefs(): Flow<Preferences> = catch { e ->
        if (e is IOException) {
            Log.e("UserPrefsRepo", "Error reading preferences.", e)
            emit(emptyPreferences())
        } else throw e
    }

    private companion object {
        val IS_LINEAR_LAYOUT = booleanPreferencesKey("is_linear_layout")
        val SORT_ORDER = stringPreferencesKey("sort_order")
        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
        val FAVORITES = stringSetPreferencesKey("favorites")
    }
}

