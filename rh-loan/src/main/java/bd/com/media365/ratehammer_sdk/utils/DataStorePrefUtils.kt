package bd.com.media365.ratehammer_sdk.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.clear
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.core.remove
import androidx.datastore.preferences.createDataStore
import bd.com.media365.ratehammer_sdk.constants.AppConstants.DataStorePref.myPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStorePrefUtils(context: Context) {

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = myPreferences
    )

    //Save Value to ModelFields Store
    suspend fun saveValue(key: String, value: Any?) {
        dataStore.edit { preferences ->
            when (value) {
                is Int? -> {
                    preferences[preferencesKey<Int>(key)] = value!!
                }

                is String? -> {
                    preferences[preferencesKey<String>(key)] = value!!
                }

                is Float? -> {
                    preferences[preferencesKey<Float>(key)] = value!!
                }

                is Double? -> {
                    preferences[preferencesKey<Double>(key)] = value!!
                }

                is Long? -> {
                    preferences[preferencesKey<Long>(key)] = value!!
                }

                is Boolean? -> {
                    preferences[preferencesKey<Boolean>(key)] = value!!
                }
            }

        }
    }

    //Get Value to ModelFields Store
    fun getIntValue(key: String): Flow<Int?> {
        return dataStore.data.map { preferences ->
            preferences[preferencesKey<Int>(key)]
        }
    }

    fun getStringValue(key: String): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[preferencesKey<String>(key)]
        }
    }

    fun getFloatValue(key: String): Flow<Float?> {
        return dataStore.data.map { preferences ->
            preferences[preferencesKey<Float>(key)]
        }
    }

    fun getDoubleValue(key: String): Flow<Double?> {
        return dataStore.data.map { preferences ->
            preferences[preferencesKey<Double>(key)]
        }
    }

    fun getLongValue(key: String): Flow<Long?> {
        return dataStore.data.map { preferences ->
            preferences[preferencesKey<Long>(key)]
        }
    }

    fun getBooleanValue(key: String): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[preferencesKey<Boolean>(key)] ?: false
        }
    }

    suspend fun removeKey(key: String) {
        dataStore.edit { preferences -> preferences.remove(preferencesKey<String>(key)) }
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }
}