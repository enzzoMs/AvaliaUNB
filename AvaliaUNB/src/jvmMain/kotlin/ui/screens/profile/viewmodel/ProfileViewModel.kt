package ui.screens.profile.viewmodel

import androidx.compose.ui.graphics.toComposeImageBitmap
import data.models.UserModel
import data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import utils.resources.Strings
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.filechooser.FileNameExtensionFilter

private const val DEFAULT_PROFILE_PIC_PATH = "src/jvmMain/resources/images/person.png"
private const val REGISTRATION_NUMBER_LENGTH = 9
private const val FIELD_MAX_LENGTH = 100

class ProfileViewModel(
    private var userModel: UserModel,
    private val userRepository: UserRepository
) {

    private val _profileUiState = MutableStateFlow(
        userModel.run {
            ProfileUiState(
                registrationNumber = registrationNumber,
                name = name,
                course = if (course == "null") "" else course,
                email = email,
                password = password,
                profilePic = profilePicture
            )
        }
    )
    val profileUiState = _profileUiState.asStateFlow()

    fun updateUser(): UserModel? {
        if (validateProfileState()) {
            val updateResult = userRepository.update(
                oldRegistrationNumber = userModel.registrationNumber,
                updatedUserModel = _profileUiState.value.run {
                    UserModel(
                        registrationNumber,
                        name,
                        course,
                        email,
                        password,
                        profilePic
                    )
                }
            )

            when (updateResult) {
                is UserRepository.UserModificationResult.Success -> {
                    setIsEditingFields(false)
                    return _profileUiState.value.run {
                        UserModel(
                            registrationNumber,
                            name,
                            course,
                            email,
                            password,
                            profilePic
                        )
                    }
                }
                is UserRepository.UserModificationResult.Failure -> {
                    _profileUiState.update { profileUiState ->
                        profileUiState.copy(
                            registrationNumberAlreadyInUse = updateResult.registrationNumberAlreadyInUse,
                            emailAlreadyInUse = updateResult.emailAlreadyInUse
                        )
                    }
                }
            }
        }

        return null
    }

    private fun validateProfileState(): Boolean {
        _profileUiState.update { profileUiState ->
            profileUiState.copy(
                invalidRegistrationNumber = profileUiState.registrationNumber.length != REGISTRATION_NUMBER_LENGTH,
                invalidName = profileUiState.name.isEmpty(),
                invalidEmail = profileUiState.email.isEmpty(),
                invalidPassword = profileUiState.password.isEmpty()
            )
        }

        return !_profileUiState.value.run {
            invalidRegistrationNumber || invalidName || invalidEmail || invalidPassword
        }
    }

    fun deleteUser() {
        userRepository.delete(userModel.registrationNumber)
    }

    fun cancelEditingUser() {
        _profileUiState.update { profileUiState ->
            userModel.run {
                profileUiState.copy(
                    registrationNumber = registrationNumber,
                    name = name,
                    course = if (course == "null") "" else course,
                    email = email,
                    password = password,
                    invalidRegistrationNumber = false,
                    invalidName = false,
                    invalidEmail = false,
                    invalidPassword = false,
                    registrationNumberAlreadyInUse = false,
                    emailAlreadyInUse = false,
                    isEditingFields = false,
                    showRemovePictureButton = false
                )
            }
        }
    }


    fun editUserProfilePicture() {
        val fileChooser = JFileChooser()
        fileChooser.currentDirectory = File(System.getProperty("user.home"))
        fileChooser.isAcceptAllFileFilterUsed = false
        fileChooser.dialogTitle = Strings.ACTION_PICK_IMAGE
        fileChooser.fileFilter = FileNameExtensionFilter("Imagens", "png", "jpeg", "bmp")

        val fileChooserAnswer = fileChooser.showOpenDialog(null)

        if (fileChooserAnswer == JFileChooser.APPROVE_OPTION) {
            if (fileChooser.selectedFile.exists()) {
                val imageFile = File(fileChooser.selectedFile.path)

                try {
                    val bufferedImage = ImageIO.read(imageFile)
                    val image = bufferedImage.toComposeImageBitmap()

                    _profileUiState.update { profileUiState ->
                        profileUiState.copy(
                            profilePic = image,
                            showRemovePictureButton = true
                        )
                    }

                } catch (e: Exception) {
                    JOptionPane.showMessageDialog(
                        null,
                        Strings.ERROR_UNABLE_TO_LOAD_IMAGE,
                        "Erro",
                        JOptionPane.ERROR_MESSAGE)
                }
            }
            else {
                JOptionPane.showMessageDialog(
                    null,
                    Strings.ERROR_FILE_DOES_NOT_EXIST,
                    "Erro",
                    JOptionPane.ERROR_MESSAGE)
            }

        }
    }

    fun removeProfilePicture() {
        _profileUiState.update { profileUiState ->
            val bufferedImage = ImageIO.read(File(DEFAULT_PROFILE_PIC_PATH))
            val defaultProfilePic = bufferedImage.toComposeImageBitmap()

            profileUiState.copy(
                profilePic = defaultProfilePic,
                showRemovePictureButton = false
            )
        }
    }

    fun updateRegistrationNumber(newRegistrationNumber: String) {
        _profileUiState.update { profileUiState ->
            val registrationNumber =  newRegistrationNumber.filter { char -> char.isDigit() }

            if (registrationNumber.length <= REGISTRATION_NUMBER_LENGTH) {
                profileUiState.copy(
                    registrationNumber = registrationNumber,
                    invalidRegistrationNumber = false,
                    registrationNumberAlreadyInUse = false
                )
            } else {
                profileUiState
            }
        }
    }

    fun setIsEditingFields(isEditing: Boolean) {
        _profileUiState.update { profileUiState ->
            profileUiState.copy(
                isEditingFields = isEditing,
                showRemovePictureButton = isEditing
            )
        }
    }

    fun updateName(newName: String) {
        if (newName.length <= FIELD_MAX_LENGTH) {
            _profileUiState.update { profileUiState ->
                profileUiState.copy(
                    name = newName,
                    invalidName = false
                )
            }
        }
    }

    fun updateCourse(newCourse: String) {
        if (newCourse.length <= FIELD_MAX_LENGTH) {
            _profileUiState.update { profileUiState ->
                profileUiState.copy(
                    course = newCourse.ifEmpty { null }
                )
            }
        }
    }

    fun updateEmail(newEmail: String) {
        if (newEmail.length <= FIELD_MAX_LENGTH) {
            _profileUiState.update { profileUiState ->
                profileUiState.copy(
                    email = newEmail,
                    invalidEmail = false,
                    emailAlreadyInUse = false
                )
            }
        }
    }

    fun updatePassword(newPassword: String) {
        if (newPassword.length <= FIELD_MAX_LENGTH) {
            _profileUiState.update { profileUiState ->
                profileUiState.copy(
                    password = newPassword,
                    invalidPassword = false
                )
            }
        }
    }
}