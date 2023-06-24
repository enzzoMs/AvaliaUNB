package ui.screens.register.viewmodel

import data.models.UserModel
import data.repositories.UserRepository
import data.repositories.UserRepository.SaveUserResult.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

const val REGISTRATION_NUMBER_LENGTH = 9
const val FIELD_MAX_LENGTH = 100

class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) {
    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState = _registerUiState.asStateFlow()

    fun registerUser(): Boolean {
        if (validateRegisterState()) {
            val saveResult = userRepository.save(_registerUiState.value.run {
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
                else -> {
                    val isRegistrationNumberAlreadyInUse =
                        saveResult is RegistrationNumberAlreadyInUse

                    val isEmailAlreadyInUse =
                        saveResult is EmailAlreadyInUse

                    val isEmailAndRegistrationNumberInUse =
                        saveResult is EmailAndRegistrationNumberInUse

                    _registerUiState.update { registerUiState ->
                        registerUiState.copy(
                            registrationNumberAlreadyInUse =
                                isRegistrationNumberAlreadyInUse || isEmailAndRegistrationNumberInUse,
                            emailAlreadyInUse =
                                isEmailAlreadyInUse || isEmailAndRegistrationNumberInUse
                        )
                    }
                }
            }

        }

        return false
    }

    private fun validateRegisterState(): Boolean {
        _registerUiState.update { registerUiState ->
            registerUiState.copy(
                invalidRegistrationNumber = registerUiState.registrationNumber.length != REGISTRATION_NUMBER_LENGTH,
                invalidName = registerUiState.name.isEmpty(),
                invalidEmail = registerUiState.email.isEmpty(),
                invalidPassword = registerUiState.password.isEmpty()
            )
        }

        return !_registerUiState.value.run {
            invalidRegistrationNumber || invalidName || invalidEmail || invalidPassword
        }
    }

    fun updateRegistrationNumber(newRegistrationNumber: String) {
        _registerUiState.update { registerUiState ->
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
            _registerUiState.update { registerUiState ->
                registerUiState.copy(
                    name = newName,
                    invalidName = false
                )
            }
        }
    }

    fun updateCourse(newCourse: String) {
        if (newCourse.length <= FIELD_MAX_LENGTH) {
            _registerUiState.update { registerUiState ->
                registerUiState.copy(
                    course = newCourse.ifEmpty { null },
                )
            }
        }
    }

    fun updateEmail(newEmail: String) {
        if (newEmail.length <= FIELD_MAX_LENGTH) {
            _registerUiState.update { registerUiState ->
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
            _registerUiState.update { registerUiState ->
                registerUiState.copy(
                    password = newPassword,
                    invalidPassword = false
                )
            }
        }
    }
}