package ui.screens.login.viewmodel

data class LoginUiState(
    val registrationNumber: String = "",
    val password: String = "",
    val invalidRegistrationNumber: Boolean = false,
    val invalidPassword: Boolean = false,
    val userNotRegistered: Boolean = false,
    val wrongPassword: Boolean = false
)