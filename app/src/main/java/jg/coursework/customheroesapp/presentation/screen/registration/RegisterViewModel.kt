package jg.coursework.customheroesapp.presentation.screen.registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jg.coursework.customheroesapp.data.dto.AuthResponseDTO
import jg.coursework.customheroesapp.data.dto.RegisterDTO.RegisterRequestDTO
import jg.coursework.customheroesapp.data.local.DataStoreManager
import jg.coursework.customheroesapp.data.local.TokenManager
import jg.coursework.customheroesapp.data.repository.CustomHeroesAuthRepository
import jg.coursework.customheroesapp.data.repository.CustomHeroesUserRepository
import jg.coursework.customheroesapp.domain.model.RegisterInputValidationType
import jg.coursework.customheroesapp.domain.use_case.ValidateRegisterInputUseCase
import jg.coursework.customheroesapp.presentation.state.RegisterState
import jg.coursework.customheroesapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val validateRegisterInputUseCase: ValidateRegisterInputUseCase,
    private val customHeroesAuthRepository: CustomHeroesAuthRepository,
    private val customHeroesUserRepository: CustomHeroesUserRepository,
    private val dataStoreManager: DataStoreManager,
    private val tokenManager: TokenManager

): ViewModel() {

    private val _authState = MutableStateFlow<Resource<AuthResponseDTO>>(Resource.loading(null))
    val authState: StateFlow<Resource<AuthResponseDTO>> = _authState

    var registerState by mutableStateOf(RegisterState())
        private set

    /** изменение логина в поле ввода логина*/
    fun onEmailInputChange(newValue: String) {
        registerState = registerState.copy(emailInput = newValue)
        checkInputValidation()
    }

    /** изменение пароля в поле ввода пароля */
    fun onPasswordInputChange(newValue: String) {
        registerState = registerState.copy(passwordInput = newValue)
        checkInputValidation()
    }

    /** изменение пароля в поле ввода повторного пароля*/
    fun onPasswordRepeatedInputChange(newValue: String) {
        registerState = registerState.copy(passwordRepeatedInput = newValue)
        checkInputValidation()
    }

    /** изменение отображения пароля в поле ввода пароля*/
    fun onToggleVisualTransformationPassword() {
        registerState = registerState.copy(isPasswordShown = !registerState.isPasswordShown)
    }

    /** изменение отображения пароля в поле ввода повторного пароля*/
    fun onToggleVisualTransformationPasswordRepeated() {
        registerState =
            registerState.copy(isPasswordRepeatedShown = !registerState.isPasswordRepeatedShown)
    }

    /** отправка логина и пароля на сервер */
    fun onRegisterClick() = viewModelScope.launch {
        val registerResponse =
            customHeroesAuthRepository.register(
                RegisterRequestDTO(
                    login = registerState.emailInput,
                    password = registerState.passwordInput
                )
            )
        _authState.value = Resource.success(registerResponse.data)
        val tokens = registerResponse.data ?: return@launch
        setTokens(tokens)
        setUserInfo()
    }

    /** проверка, все ли формы заполнены корректно*/
    private fun checkInputValidation() {
        val validationResult = validateRegisterInputUseCase(
            registerState.emailInput,
            registerState.passwordInput,
            registerState.passwordRepeatedInput
        )
        processInputValidationType(validationResult)
    }

    /** ошибки валидации*/
    private fun processInputValidationType(type: RegisterInputValidationType) {
        registerState = when (type) {
            RegisterInputValidationType.EmptyField -> {
                registerState.copy(errorMessageInput = "Заполните все поля", isInputValid = false)
            }

            RegisterInputValidationType.NoEmail -> {
                registerState.copy(
                    errorMessageInput = "Проверьте корректность почты",
                    isInputValid = false
                )
            }

            RegisterInputValidationType.PasswordTooShort -> {
                registerState.copy(
                    errorMessageInput = "Пароль должен быть не менее 8 символов",
                    isInputValid = false
                )
            }

            RegisterInputValidationType.PasswordsDoNotMatch -> {
                registerState.copy(errorMessageInput = "Пароли не совападают", isInputValid = false)
            }

            RegisterInputValidationType.PasswordUpperCaseMissing -> {
                registerState.copy(
                    errorMessageInput = "В пароле должна быть щаглавная буква",
                    isInputValid = false
                )
            }

            RegisterInputValidationType.PasswordSpecialCharMissing -> {
                registerState.copy(
                    errorMessageInput = "В пароле должен быть хотя бы один специальный символ",
                    isInputValid = false
                )
            }

            RegisterInputValidationType.PasswordNumberMissing -> {
                registerState.copy(
                    errorMessageInput = "В пароле должна быть хотя бы одна цифра",
                    isInputValid = false
                )
            }

            RegisterInputValidationType.Valid -> {
                registerState.copy(errorMessageInput = null, isInputValid = true)
            }
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
}