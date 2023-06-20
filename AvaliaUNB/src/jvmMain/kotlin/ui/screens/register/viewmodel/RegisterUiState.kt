package ui.screens.register.viewmodel

data class RegisterUiState(
    val registrationNumber: String = "",
    val name: String = "",
    val course: String = "",
    val email: String = "",
    val password: String = "",
    val invalidRegistrationNumber: Boolean = false,
    val invalidName: Boolean = false,
    val invalidCourse: Boolean = false,
    val invalidEmail: Boolean = false,
    val invalidPassword: Boolean = false
)