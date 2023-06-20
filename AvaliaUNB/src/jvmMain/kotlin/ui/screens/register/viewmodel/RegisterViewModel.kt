package ui.screens.register.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

const val REGISTRATION_NUMBER_LENGTH = 9

class RegisterViewModel {
    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState = _registerUiState.asStateFlow()

    fun registerUser() {
        validateRegisterState()
    }

    private fun validateRegisterState() {
        _registerUiState.update { registerUiState ->
            registerUiState.copy(
                invalidRegistrationNumber = registerUiState.registrationNumber.length != REGISTRATION_NUMBER_LENGTH,
                invalidName = registerUiState.name.isEmpty(),
                invalidCourse = registerUiState.course.isEmpty(),
                invalidEmail = registerUiState.email.isEmpty(),
                invalidPassword = registerUiState.password.isEmpty()
            )
        }
    }

    fun updateRegistrationNumber(newRegistrationNumber: String) {
        _registerUiState.update { registerUiState ->
            val registrationNumber =  newRegistrationNumber.filter { char -> char.isDigit() }

            if (registrationNumber.length <= REGISTRATION_NUMBER_LENGTH) {
                registerUiState.copy(
                    registrationNumber = registrationNumber,
                    invalidRegistrationNumber = false
                )
            } else {
                registerUiState
            }
        }
    }

    fun updateName(newName: String) {
        _registerUiState.update { registerUiState ->
            registerUiState.copy(
                name = newName,
                invalidName = false
            )
        }
    }

    fun updateCourse(newCourse: String) {
        _registerUiState.update { registerUiState ->
            registerUiState.copy(
                course = newCourse,
                invalidCourse = false
            )
        }
    }

    fun updateEmail(newEmail: String) {
        _registerUiState.update { registerUiState ->
            registerUiState.copy(
                email = newEmail,
                invalidEmail = false
            )
        }
    }

    fun updatePassword(newPassword: String) {
        _registerUiState.update { registerUiState ->
            registerUiState.copy(
                password = newPassword,
                invalidPassword = false
            )
        }
    }
}