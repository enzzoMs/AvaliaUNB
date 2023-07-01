package data.source

import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.graphics.toComposeImageBitmap
import data.models.UserModel
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDAO @Inject constructor(
    private val database: DatabaseManager
) {

    fun insertUser(user: UserModel) {
        val defaultProfilePic = user.profilePicture.toAwtImage()
        val stream = ByteArrayOutputStream()
        ImageIO.write(defaultProfilePic, "png", stream)
        val defaultProfilePicBytes = stream.toByteArray()

        val userInsertStatement = "INSERT INTO avalia_unb.usuario VALUES (?, ?, ?, ?, ?, ?)"

        val preparedStatement = database.prepareStatement(userInsertStatement)
        user.apply {
            preparedStatement.setString(1, registrationNumber)
            preparedStatement.setString(2, name)
            preparedStatement.setString(3, course)
            preparedStatement.setString(4, email)
            preparedStatement.setString(5, password)
            preparedStatement.setBytes(6, defaultProfilePicBytes)
        }

        preparedStatement.execute()
    }

    fun updateUser(oldRegistrationNumber: String, updatedUserModel: UserModel) {
        val userUpdateStatement =
            "UPDATE avalia_unb.usuario " +
            "SET matricula = ?, nome = ?, curso = ?, email = ?, " +
            "senha = ?, foto_de_perfil = ? WHERE matricula = '${oldRegistrationNumber}';"

        val profilePictureBytes = updatedUserModel.profilePicture.toAwtImage()
        val stream = ByteArrayOutputStream()
        ImageIO.write(profilePictureBytes, "png", stream)

        val bytes = stream.toByteArray()

        val preparedStatement = database.prepareStatement(userUpdateStatement)
        updatedUserModel.apply {
            preparedStatement.setString(1, registrationNumber)
            preparedStatement.setString(2, name)
            preparedStatement.setString(3, course)
            preparedStatement.setString(4, email)
            preparedStatement.setString(5, password)
            preparedStatement.setBytes(6, bytes)
        }

        preparedStatement.execute()
    }

    fun deleteUser(registrationNumber: String) {
        database.executeStatement(
            "DELETE FROM avalia_unb.usuario WHERE matricula = '${registrationNumber}'"
        )
    }

    fun getUser(userRegistrationNumber: String): UserModel {
        val queryResult = database.executeQuery(
            "SELECT * FROM avalia_unb.usuario WHERE matricula = '${userRegistrationNumber}'"
        )

        queryResult.next()

        val profilePicBytes = queryResult.getBytes("foto_de_perfil")
        val bufferedProfilePicImage = ImageIO.read(profilePicBytes.inputStream())
        val profilePic = bufferedProfilePicImage.toComposeImageBitmap()

        return UserModel(
            queryResult.getString("matricula"),
            queryResult.getString("nome"),
            queryResult.getString("curso"),
            queryResult.getString("email"),
            queryResult.getString("senha"),
            profilePic
        )
    }


    fun getUserPassword(registrationNumber: String): String {
        val queryResult = database.executeQuery(
            "SELECT senha FROM avalia_unb.usuario WHERE matricula = '${registrationNumber}'"
        )

        queryResult.next()

        return queryResult.getString("senha")
    }

    fun isRegistrationNumberInUse(registrationNumber: String): Boolean {
        val queryResult = database.executeQuery(
            "SELECT * FROM avalia_unb.usuario WHERE matricula = '${registrationNumber}'"
        )

        return queryResult.next()
    }

    fun isEmailInUse(email: String): Boolean {
        val queryResult = database.executeQuery(
            "SELECT * FROM avalia_unb.usuario WHERE email = '${email}'"
        )

        return queryResult.next()
    }
}