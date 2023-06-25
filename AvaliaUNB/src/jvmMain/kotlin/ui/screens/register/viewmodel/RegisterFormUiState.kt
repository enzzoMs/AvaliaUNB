package ui.screens.register.viewmodel

data class RegisterFormUiState(
    val registrationNumber: String = "",
    val name: String = "",
    val course: String? = null,
    val email: String = "",
    val password: String = "",
    val invalidRegistrationNumber: Boolean = false,
    val invalidName: Boolean = false,
    val invalidEmail: Boolean = false,
    val invalidPassword: Boolean = false,
    val registrationNumberAlreadyInUse: Boolean = false,
    val emailAlreadyInUse: Boolean = false
)