package jg.coursework.customheroesapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import jg.coursework.customheroesapp.util.AppConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Singleton
class TokenManager(private val appContext: Context) {

    companion object {
        private val Context.dataStore by preferencesDataStore(AppConstants.TOKEN_DATASTORE)
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    suspend fun setAccessToken(accessToken: String) {
        appContext.dataStore.edit { tokenStore ->
            tokenStore[ACCESS_TOKEN] = accessToken
        }
    }

    fun getAccessToken(): Flow<String?> {
        return appContext.dataStore.data.map { tokenStore ->
            tokenStore[ACCESS_TOKEN]
        }
    }

    suspend fun setRefreshToken(refreshToken: String) {
        appContext.dataStore.edit { tokenStore ->
            tokenStore[REFRESH_TOKEN] = refreshToken
        }
    }

    fun getRefreshToken(): Flow<String?> {
        return appContext.dataStore.data.map { tokenStore ->
            tokenStore[REFRESH_TOKEN]
        }
    }
}