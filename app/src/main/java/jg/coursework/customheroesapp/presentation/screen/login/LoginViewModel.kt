package jg.coursework.customheroesapp.presentation.screen.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jg.coursework.customheroesapp.data.dto.AuthResponseDTO
import jg.coursework.customheroesapp.data.dto.LoginDTO.LoginRequestDTO
import jg.coursework.customheroesapp.data.local.DataStoreManager
import jg.coursework.customheroesapp.data.local.TokenManager
import jg.coursework.customheroesapp.domain.model.LoginInputValidationType
import jg.coursework.customheroesapp.domain.repository.IAuthRepository
import jg.coursework.customheroesapp.domain.repository.IUserRepository
import jg.coursework.customheroesapp.domain.use_case.ValidateLoginInputUseCase
import jg.coursework.customheroesapp.presentation.state.LoginState
import jg.coursework.customheroesapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateLoginInputUseCase: ValidateLoginInputUseCase,
    private val customHeroesAuthRepository: IAuthRepository,
    private val customHeroesUserRepository: IUserRepository,
    private val tokenManager: TokenManager,
    private val dataStoreManager: DataStoreManager
    ): ViewModel() {

    private val _authState = MutableStateFlow<Resource<AuthResponseDTO>>(Resource.loading(null))
    val authState: StateFlow<Resource<AuthResponseDTO>> = _authState

    var loginState by mutableStateOf(LoginState())
        private set

    /** изменения логина в поле для ввода логина*/
    fun onEmailInputChange(newValue: String) {
        loginState = loginState.copy(emailInput = newValue)
        checkInputValidation()
    }

    /** изменения пароля в поле для ввода пароля*/
    fun onPasswordInputChange(newValue: String) {
        loginState = loginState.copy(passwordInput = newValue)
        checkInputValidation()
    }

    /** отображение\сокрытие пароля */
    fun onToggleVisualTransformation() {
        loginState = loginState.copy(isPasswordShown = !loginState.isPasswordShown)
    }

    /** отправка логина и пароля на сервер */
    fun onLoginClick() = viewModelScope.launch {
        val loginResponse =
            customHeroesAuthRepository.login(LoginRequestDTO(loginState.emailInput, loginState.passwordInput))
        if(loginResponse.status == Resource.Status.SUCCESS) {
            _authState.value = loginResponse
            val nonNullTokens = loginResponse.data ?: return@launch
            setTokens(nonNullTokens)
            setUserInfo()
        }
        else {
            _authState.value = Resource.error( "Возникла ошибка")
        }

    }

    /** TODO перенести на экран профиля
     * метод записывает информацию о пользователе при логине
     * */
    private suspend fun setUserInfo() {
        val me = customHeroesUserRepository.getMe().data
        if (me != null) {
            dataStoreManager.setUserLogin(me.login)
            dataStoreManager.setUserName(me.name)
            dataStoreManager.setAvatarSourcePath(me.avatarSourcePath)
            dataStoreManager.setType(me.type)
            dataStoreManager.setUserId(me.id)
        }
    }

    /** метод записывает токены в хранилище токенов*/
    private suspend fun setTokens(tokens: AuthResponseDTO) {
        tokenManager.setAccessToken(tokens.accessToken)
        tokenManager.setRefreshToken(tokens.refreshToken)
    }

    /** проверка, все ли формы заполнены корректно*/
    private fun checkInputValidation() {
        val validationResult = validateLoginInputUseCase(
            loginState.emailInput,
            loginState.passwordInput
        )
        processInputValidationType(validationResult)
    }

    /** Ошибки валидации*/
    private fun processInputValidationType(type: LoginInputValidationType) {
        loginState = when(type) {
            LoginInputValidationType.EmptyField -> {
                loginState.copy(errorMessageInput = "Заполните все поля", isInputValid = false)
            }
            LoginInputValidationType.NoEmail -> {
                loginState.copy(errorMessageInput = "Проверьте корерктность почты", isInputValid = false)
            }
            LoginInputValidationType.Valid -> {
                loginState.copy(errorMessageInput = null, isInputValid = true)
            }
        }
    }

}