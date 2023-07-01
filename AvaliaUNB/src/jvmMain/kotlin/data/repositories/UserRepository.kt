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

    fun getUser(userRegistrationNumber: String): UserModel = userDAO.getUser(userRegistrationNumber)

    fun save(user: UserModel): UserModificationResult {
        val isRegistrationNumberInUse = userDAO.isRegistrationNumberInUse(user.registrationNumber)
        val isEmailInUse = userDAO.isEmailInUse(user.email)

        return when {
            isRegistrationNumberInUse || isEmailInUse -> UserModificationResult.Failure(
                registrationNumberAlreadyInUse = isRegistrationNumberInUse,
                emailAlreadyInUse = isEmailInUse
            )
            else -> {
                userDAO.insertUser(user)
                UserModificationResult.Success
            }
        }
    }

    fun delete(registrationNumber: String) {
        userDAO.deleteUser(registrationNumber)
    }

    fun update(oldRegistrationNumber: String, updatedUserModel: UserModel): UserModificationResult {
        val oldUser = userDAO.getUser(oldRegistrationNumber)

        val registrationNumberModified = oldRegistrationNumber != updatedUserModel.registrationNumber
        val emailModified = oldUser.email != updatedUserModel.email

        if (registrationNumberModified || emailModified) {
            val isRegistrationNumberInUse = userDAO.isRegistrationNumberInUse(updatedUserModel.registrationNumber)
            val isEmailInUse = userDAO.isEmailInUse(updatedUserModel.email)

            if (isRegistrationNumberInUse || isEmailInUse) {
                return UserModificationResult.Failure(
                    registrationNumberAlreadyInUse = isRegistrationNumberInUse,
                    emailAlreadyInUse = isEmailInUse
                )
            }
        }

        userDAO.updateUser(oldRegistrationNumber, updatedUserModel)
        return UserModificationResult.Success
    }


    sealed class UserModificationResult {
        object Success : UserModificationResult()
        data class Failure(
            val registrationNumberAlreadyInUse: Boolean = false,
            val emailAlreadyInUse: Boolean = false
        ) : UserModificationResult()
    }

    enum class UserRegistrationStatus {
        Registered,
        NotRegistered,
        WrongPassword
    }
}