package ui.screens.register.viewmodel

import data.models.UserModel
import data.repositories.UserRepository
import data.repositories.UserRepository.SaveUserResult.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private const val REGISTRATION_NUMBER_LENGTH = 9
private const val FIELD_MAX_LENGTH = 100

class RegisterFormViewModel @Inject constructor(
    private val userRepository: UserRepository
) {
    private val _registerFormUiState = MutableStateFlow(RegisterFormUiState())
    val registerFormUiState = _registerFormUiState.asStateFlow()

    fun registerUser(): Boolean {
        if (validateRegisterState()) {
            val saveResult = userRepository.save(_registerFormUiState.value.run {
                UserModel(
                    registrationNumber,
                    name,
                    course,
                    email,
                    password
                )
            })

        when (saveResult) {
            is Success -> return true
            is Failure -> _registerFormUiState.update { registerUiState ->
                    registerUiState.copy(
                        registrationNumberAlreadyInUse = saveResult.registrationNumberAlreadyInUse,
                        emailAlreadyInUse = saveResult.emailAlreadyInUse
                    )
                }
            }
        }

        return false
    }

    private fun validateRegisterState(): Boolean {
        _registerFormUiState.update { registerUiState ->
            registerUiState.copy(
                invalidRegistrationNumber = registerUiState.registrationNumber.length != REGISTRATION_NUMBER_LENGTH,
                invalidName = registerUiState.name.isEmpty(),
                invalidEmail = registerUiState.email.isEmpty(),
                invalidPassword = registerUiState.password.isEmpty()
            )
        }

        return !_registerFormUiState.value.run {
            invalidRegistrationNumber || invalidName || invalidEmail || invalidPassword
        }
    }

    fun updateRegistrationNumber(newRegistrationNumber: String) {
        _registerFormUiState.update { registerUiState ->
            val registrationNumber =  newRegistrationNumber.filter { char -> char.isDigit() }

            if (registrationNumber.length <= REGISTRATION_NUMBER_LENGTH) {
                registerUiState.copy(
                    registrationNumber = registrationNumber,
                    invalidRegistrationNumber = false,
                    registrationNumberAlreadyInUse = false
                )
            } else {
                registerUiState
            }
        }
    }

    fun updateName(newName: String) {
        if (newName.length <= FIELD_MAX_LENGTH) {
            _registerFormUiState.update { registerUiState ->
                registerUiState.copy(
                    name = newName,
                    invalidName = false
                )
            }
        }
    }

    fun updateCourse(newCourse: String) {
        if (newCourse.length <= FIELD_MAX_LENGTH) {
            _registerFormUiState.update { registerUiState ->
                registerUiState.copy(
                    course = newCourse.ifEmpty { null },
                )
            }
        }
    }

    fun updateEmail(newEmail: String) {
        if (newEmail.length <= FIELD_MAX_LENGTH) {
            _registerFormUiState.update { registerUiState ->
                registerUiState.copy(
                    email = newEmail,
                    invalidEmail = false,
                    emailAlreadyInUse = false
                )
            }
        }
    }

    fun updatePassword(newPassword: String) {
        if (newPassword.length <= FIELD_MAX_LENGTH) {
            _registerFormUiState.update { registerUiState ->
                registerUiState.copy(
                    password = newPassword,
                    invalidPassword = false
                )
            }
        }
    }
}