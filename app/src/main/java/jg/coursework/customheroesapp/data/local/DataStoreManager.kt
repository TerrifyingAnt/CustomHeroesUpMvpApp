package jg.coursework.customheroesapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import jg.coursework.customheroesapp.data.dto.User
import jg.coursework.customheroesapp.util.AppConstants
import jg.coursework.customheroesapp.util.AppConstants.DEFAULT_AVATAR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext private val appContext: Context
) {
    companion object {
        private val Context.dataStore by preferencesDataStore(AppConstants.USER_DATASTORE)
        private val USER_LOGIN = stringPreferencesKey("user_login")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_ID = intPreferencesKey("user_id")
        private val USER_PHONE_NUMBER = stringPreferencesKey("user_phone_number")
        private val AVATAR_SOURCE_PATH = stringPreferencesKey("avatar_source_path")
        private val TYPE = stringPreferencesKey("type")
        private val BASKET = stringPreferencesKey("basket")
    }

    suspend fun setUserLogin(login: String) {
        appContext.dataStore.edit { localStorage ->
            localStorage[USER_LOGIN] = login
        }
    }

    fun getUserLogin(): Flow<String?> {
        return appContext.dataStore.data.map { localStorage ->
            localStorage[USER_LOGIN]
        }
    }

    suspend fun setUserName(name: String) {
        appContext.dataStore.edit { localStorage ->
            localStorage[USER_NAME] = name
        }
    }

    fun getUserName(): Flow<String?> {
        return appContext.dataStore.data.map { localStorage ->
            localStorage[USER_NAME]
        }
    }

    suspend fun setUserId(id: Int) {
        appContext.dataStore.edit { localStorage ->
            localStorage[USER_ID] = id
        }
    }

    fun getUserId(): Flow<Int?> {
        return appContext.dataStore.data.map { localStorage ->
            localStorage[USER_ID]
        }
    }

    suspend fun setUserPhoneNumber(userPhoneNumber: String) {
        appContext.dataStore.edit { localStorage ->
            localStorage[USER_PHONE_NUMBER] = userPhoneNumber
        }
    }

    fun getUserPhoneNumber(): Flow<String?> {
        return appContext.dataStore.data.map { localStorage ->
            localStorage[USER_PHONE_NUMBER]
        }
    }

    suspend fun setAvatarSourcePath(avatarSourcePath: String) {
        appContext.dataStore.edit { localStorage ->
            localStorage[AVATAR_SOURCE_PATH] = avatarSourcePath
        }
    }

    fun getAvatarSourcePath(): Flow<String?> {
        return appContext.dataStore.data.map { localStorage ->
            localStorage[AVATAR_SOURCE_PATH]
        }
    }

    suspend fun setType(type: String) {
        appContext.dataStore.edit { localStorage ->
            localStorage[TYPE] = type
        }
    }

    fun getType(): Flow<String?> {
        return appContext.dataStore.data.map { localStorage ->
            localStorage[TYPE]
        }
    }

    suspend fun getUserInfo(): User? {
        var user: User? = null
        val id = appContext.dataStore.data.map { localStorage ->
            localStorage[USER_ID]
        }.first()
        val name = appContext.dataStore.data.map { localStorage ->
            localStorage[USER_NAME]
        }.first()
        val login = appContext.dataStore.data.map { localStorage ->
            localStorage[USER_LOGIN]
        }.first()
        val phoneNumber = appContext.dataStore.data.map { localStorage ->
            localStorage[USER_PHONE_NUMBER]
        }.first()
        val type = appContext.dataStore.data.map { localStorage ->
            localStorage[TYPE]
        }.first()
        val avatarSourcePath = appContext.dataStore.data.map { localStorage ->
            localStorage[AVATAR_SOURCE_PATH]
        }.first()

        if(id != null && name != null && login != null && phoneNumber != null && type != null) {
            user = User(id, name, login, "", phoneNumber, avatarSourcePath ?: DEFAULT_AVATAR, type)
        }

        return user
    }

    // TODO - в другой класс, а потом в бд вообще перенсти бд корзину
    suspend fun setBasket(basket: String) {
        appContext.dataStore.edit { localStorage ->
            localStorage[BASKET] = basket
        }
    }

    fun getBasket(): Flow<String?> {
        return appContext.dataStore.data.map { localStorage ->
            localStorage[BASKET]
        }
    }


}