package ui.screens.login.viewmodel

import data.repositories.UserRepository
import data.repositories.UserRepository.UserRegistrationStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private const val REGISTRATION_NUMBER_LENGTH = 9
private const val FIELD_MAX_LENGTH = 100

class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) {
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    fun login(): Boolean {
        if (validateLoginState()) {
            val userRegistrationStatus = userRepository.isUserRegistered(
                _loginUiState.value.registrationNumber,
                _loginUiState.value.password
            )

            when (userRegistrationStatus) {
                UserRegistrationStatus.Registered -> return true
                UserRegistrationStatus.NotRegistered -> _loginUiState.update { loginUiState ->
                    loginUiState.copy(
                        userNotRegistered = true,
                        wrongPassword = false
                    )
                }
                UserRegistrationStatus.WrongPassword -> _loginUiState.update { loginUiState ->
                    loginUiState.copy(wrongPassword = true)
                }
            }
        }

        return false
    }

    private fun validateLoginState(): Boolean {
        _loginUiState.update { loginUiState ->
            loginUiState.copy(
                invalidRegistrationNumber = loginUiState.registrationNumber.length != REGISTRATION_NUMBER_LENGTH,
                invalidPassword = loginUiState.password.isEmpty(),
            )
        }

        return !_loginUiState.value.run {
            invalidRegistrationNumber || invalidPassword
        }
    }


    fun updateRegistrationNumber(newRegistrationNumber: String) {
        _loginUiState.update { loginUiState ->
            val registrationNumber =  newRegistrationNumber.filter { char -> char.isDigit() }

            if (registrationNumber.length <= REGISTRATION_NUMBER_LENGTH) {
                loginUiState.copy(
                    registrationNumber = registrationNumber,
                    invalidRegistrationNumber = false,
                    userNotRegistered = false
                )
            } else {
                loginUiState
            }
        }
    }

    fun updatePassword(newPassword: String) {
        if (newPassword.length <= FIELD_MAX_LENGTH) {
            _loginUiState.update { loginUiState ->
                loginUiState.copy(
                    password = newPassword,
                    invalidPassword = false,
                    wrongPassword = false
                )
            }
        }
    }
}