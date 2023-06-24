package data.repositories

import data.models.UserModel
import data.source.UserDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDAO: UserDAO
) {

    fun save(user: UserModel): SaveUserResult {
        val isUserRegistered = userDAO.isUserRegistered(user.registrationNumber)
        val isEmailRegistered = userDAO.isEmailRegistered(user.email)

        return when {
            isUserRegistered && isEmailRegistered -> SaveUserResult.EmailAndRegistrationNumberInUse()
            isUserRegistered -> SaveUserResult.RegistrationNumberAlreadyInUse()
            isEmailRegistered -> SaveUserResult.EmailAlreadyInUse()
            else -> {
                userDAO.insertUser(user)
                SaveUserResult.Success()
            }
        }
    }

    sealed class SaveUserResult {
        data class Success(
            val message: String = "User saved successfully"
        ) : SaveUserResult()

        data class EmailAlreadyInUse(
            val errorMessage: String = "Email already in use"
        ) : SaveUserResult()

        data class RegistrationNumberAlreadyInUse(
            val errorMessage: String = "Registration number already in use"
        ) : SaveUserResult()

        data class EmailAndRegistrationNumberInUse(
            val errorMessage: String = "Both the email and registration number are already in use"
        ) : SaveUserResult()
    }
}