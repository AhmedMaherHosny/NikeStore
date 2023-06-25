package com.example.local.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.core.Constants.NIKE_PREFERENCES
import com.example.domain.models.AppUserDomainModel
import com.example.domain.repository.local.DatastoreRepository
import com.example.local.mappers.toAppUser
import com.example.local.mappers.toAppUserLocal
import com.example.local.models.AppUserLocalModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = NIKE_PREFERENCES)

class DatastoreRepositoryImpl @Inject constructor(
    private val context: Context,
    private val gson: Gson
) : DatastoreRepository {
    override suspend fun writeUserModel(key: String, value: AppUserDomainModel) {
        val preferencesKey = stringPreferencesKey(key)
        val serializedData = gson.toJson(value.toAppUserLocal())
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = serializedData
        }
    }

    override suspend fun readUserModel(key: String): AppUserDomainModel? {
        val preferencesKey = stringPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        val serializedData = preferences[preferencesKey]
        return if (serializedData != null)
            gson.fromJson(serializedData, AppUserLocalModel::class.java).toAppUser()
        else
            null
    }

    override suspend fun deleteUserModel(key: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences.remove(preferencesKey)
        }
    }
}