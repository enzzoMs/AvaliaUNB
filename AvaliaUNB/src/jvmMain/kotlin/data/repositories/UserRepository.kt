package data.repositories

import data.models.UserModel
import data.source.UserDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDAO: UserDAO
) {
    fun isUserRegistered(registrationNumber: String, password: String): UserRegistrationStatus {
        val isRegistrationNumberInUse = userDAO.isRegistrationNumberInUse(registrationNumber)

        return when {
            !isRegistrationNumberInUse -> UserRegistrationStatus.NotRegistered
            userDAO.getUserPassword(registrationNumber) != password -> UserRegistrationStatus.WrongPassword
            else -> UserRegistrationStatus.Registered
        }
    }

    fun save(user: UserModel): SaveUserResult {
        val isRegistrationNumberInUse = userDAO.isRegistrationNumberInUse(user.registrationNumber)
        val isEmailInUse = userDAO.isEmailInUse(user.email)

        return when {
            isRegistrationNumberInUse || isEmailInUse -> SaveUserResult.Failure(
                registrationNumberAlreadyInUse = isRegistrationNumberInUse,
                emailAlreadyInUse = isEmailInUse
            )
            else -> {
                userDAO.insertUser(user)
                SaveUserResult.Success
            }
        }
    }

    sealed class SaveUserResult {
        object Success : SaveUserResult()
        data class Failure(
            val registrationNumberAlreadyInUse: Boolean = false,
            val emailAlreadyInUse: Boolean = false
        ) : SaveUserResult()
    }

    enum class UserRegistrationStatus {
        Registered,
        NotRegistered,
        WrongPassword
    }
}