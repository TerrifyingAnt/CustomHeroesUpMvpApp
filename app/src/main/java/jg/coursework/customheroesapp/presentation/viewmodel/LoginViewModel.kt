package jg.coursework.customheroesapp.presentation.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jg.coursework.customheroesapp.data.dto.LoginDTO.LoginRequestDTO
import jg.coursework.customheroesapp.data.dto.AuthResponseDTO
import jg.coursework.customheroesapp.domain.model.LoginInputValidationType
import jg.coursework.customheroesapp.domain.repository.CustomHeroesRepository
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
    private val customHeroesRepository: CustomHeroesRepository,
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    private val _authState = MutableStateFlow<Resource<AuthResponseDTO>>(Resource.loading(null))
    val authState: StateFlow<Resource<AuthResponseDTO>> = _authState

    var loginState by mutableStateOf(LoginState())
        private set

    fun onEmailInputChange(newValue: String) {
        loginState = loginState.copy(emailInput = newValue)
        checkInputValidation()
    }

    fun onPasswordInputChange(newValue: String) {
        loginState = loginState.copy(passwordInput = newValue)
        checkInputValidation()
    }

    fun onToggleVisualTransformation() {
        loginState = loginState.copy(isPasswordShown = !loginState.isPasswordShown)
    }

    fun onLoginClick() {
        viewModelScope.launch {
            try {
                val loginResponse =
                    customHeroesRepository.login(LoginRequestDTO(loginState.emailInput, loginState.passwordInput))
                _authState.value = Resource.success(loginResponse.body())
                sharedPreferences.edit().putString("token", loginResponse.body()?.accessToken).apply()
            } catch (e: Exception) {
                _authState.value = Resource.error(e.message ?: "Возникла ошибка")
            }
        }
    }

    private fun checkInputValidation() {
        val validationResult = validateLoginInputUseCase(
            loginState.emailInput,
            loginState.passwordInput
        )
        processInputValidationType(validationResult)
    }

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