package jg.coursework.customheroesapp.presentation.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jg.coursework.customheroesapp.data.dto.AuthResponseDTO
import jg.coursework.customheroesapp.data.dto.LoginDTO.LoginRequestDTO
import jg.coursework.customheroesapp.data.dto.RegisterDTO.RegisterRequestDTO
import jg.coursework.customheroesapp.domain.model.RegisterInputValidationType
import jg.coursework.customheroesapp.domain.repository.CustomHeroesRepository
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
    private val customHeroesRepository: CustomHeroesRepository,
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    private val _authState = MutableStateFlow<Resource<AuthResponseDTO>>(Resource.loading(null))
    val authState: StateFlow<Resource<AuthResponseDTO>> = _authState

    var registerState by mutableStateOf(RegisterState())
        private set

    fun onEmailInputChange(newValue: String) {
        registerState = registerState.copy(emailInput = newValue)
        checkInputValidation()
    }

    fun onPasswordInputChange(newValue: String) {
        registerState = registerState.copy(passwordInput = newValue)
        checkInputValidation()
    }

    fun onPasswordRepeatedInputChange(newValue: String) {
        registerState = registerState.copy(passwordRepeatedInput = newValue)
        checkInputValidation()
    }

    fun onToggleVisualTransformationPassword() {
        registerState = registerState.copy(isPasswordShown = !registerState.isPasswordShown)
    }

    fun onToggleVisualTransformationPasswordRepeated() {
        registerState = registerState.copy(isPasswordRepeatedShown = !registerState.isPasswordShown)
    }

    fun onRegisterClick() {
        viewModelScope.launch {
            try {
                val registerResponse =
                    customHeroesRepository.register(RegisterRequestDTO(login = registerState.emailInput, password = registerState.passwordInput))
                _authState.value = Resource.success(registerResponse.body())
                sharedPreferences.edit().putString("token", registerResponse.body()?.accessToken).apply()
            } catch (e: Exception) {
                _authState.value = Resource.error(e.message ?: "Возникла ошибка")
            }
        }
    }

    private fun checkInputValidation() {
        val validationResult = validateRegisterInputUseCase(
            registerState.emailInput,
            registerState.passwordInput,
            registerState.passwordRepeatedInput
        )
        processInputValidationType(validationResult)
    }

    private fun processInputValidationType(type: RegisterInputValidationType) {
        registerState = when(type) {
            RegisterInputValidationType.EmptyField -> {
                registerState.copy(errorMessageInput = "Заполните все поля", isInputValid = false)
            }
            RegisterInputValidationType.NoEmail -> {
                registerState.copy(errorMessageInput = "Проверьте корректность почты", isInputValid = false)
            }
            RegisterInputValidationType.PasswordTooShort -> {
                registerState.copy(errorMessageInput = "Пароль должен быть не менее 8 символов", isInputValid = false)
            }
            RegisterInputValidationType.PasswordsDoNotMatch -> {
                registerState.copy(errorMessageInput = "Пароли не совападают", isInputValid = false)
            }
            RegisterInputValidationType.PasswordUpperCaseMissing -> {
                registerState.copy(errorMessageInput = "В пароле должна быть щаглавная буква", isInputValid = false)
            }
            RegisterInputValidationType.PasswordSpecialCharMissing -> {
                registerState.copy(errorMessageInput = "В пароле должен быть хотя бы один специальный символ", isInputValid = false)
            }
            RegisterInputValidationType.PasswordNumberMissing -> {
                registerState.copy(errorMessageInput = "В пароле должна быть хотя бы одна цифра", isInputValid = false)
            }
            RegisterInputValidationType.Valid -> {
                registerState.copy(errorMessageInput = null, isInputValid = true)
            }



        }
    }
}