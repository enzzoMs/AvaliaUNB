package data.source

import data.models.UserModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDAO @Inject constructor(
    private val database: DatabaseManager
) {

    fun insertUser(user: UserModel) {
        database.executeStatement(
            "INSERT INTO avalia_unb.usuario VALUES ('%s', '%s', '%s', '%s', '%s');".format(
                user.registrationNumber,
                user.name,
                user.course,
                user.email,
                user.password
            )
        )
    }

    fun isUserRegistered(registrationNumber: String): Boolean {
        val queryResult = database.executeQuery(
            "SELECT * FROM avalia_unb.usuario WHERE matricula = '${registrationNumber}'"
        )

        return queryResult.next()
    }

    fun isEmailRegistered(email: String): Boolean {
        val queryResult = database.executeQuery(
            "SELECT * FROM avalia_unb.usuario WHERE email = '${email}'"
        )

        return queryResult.next()
    }
}