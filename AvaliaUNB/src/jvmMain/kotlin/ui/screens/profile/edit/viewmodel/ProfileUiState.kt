package ui.screens.profile.edit.viewmodel

import androidx.compose.ui.graphics.ImageBitmap

data class ProfileUiState (
    val registrationNumber: String = "",
    val name: String = "",
    val course: String? = null,
    val email: String = "",
    val password: String = "",
    val profilePic: ImageBitmap,
    val invalidRegistrationNumber: Boolean = false,
    val invalidName: Boolean = false,
    val invalidEmail: Boolean = false,
    val invalidPassword: Boolean = false,
    val registrationNumberAlreadyInUse: Boolean = false,
    val emailAlreadyInUse: Boolean = false,
    val isEditingFields: Boolean = false,
    val showRemovePictureButton: Boolean = false
)