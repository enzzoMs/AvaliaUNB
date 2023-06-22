package data.models

data class UserModel(
    val registrationNumber: String,
    val name: String,
    val course: String?,
    val email: String,
    val password: String
)