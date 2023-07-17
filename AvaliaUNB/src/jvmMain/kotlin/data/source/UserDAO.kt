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
        val userInsertStatement = "INSERT INTO usuario (matricula, nome, curso, email, senha) " +
                "VALUES (?, ?, ?, ?, ?)"

        val preparedStatement = database.prepareStatement(userInsertStatement)
        user.apply {
            preparedStatement.setString(1, registrationNumber)
            preparedStatement.setString(2, name)
            preparedStatement.setString(3, course)
            preparedStatement.setString(4, email)
            preparedStatement.setString(5, password)
        }

        preparedStatement.execute()
    }

    fun updateUser(oldRegistrationNumber: String, updatedUserModel: UserModel) {
        val userUpdateStatement =
            "UPDATE usuario " +
            "SET matricula = ?, nome = ?, curso = ?, email = ?, " +
            "senha = ?, foto_de_perfil = ?, eh_administrador = ? WHERE matricula = '${oldRegistrationNumber}';"

        val profilePictureBytes = if (updatedUserModel.profilePicture != null) {
            val profilePicture = updatedUserModel.profilePicture.toAwtImage()
            val stream = ByteArrayOutputStream()
            ImageIO.write(profilePicture, "png", stream)
            stream.toByteArray()
        } else {
            null
        }

        val preparedStatement = database.prepareStatement(userUpdateStatement)
        updatedUserModel.apply {
            preparedStatement.setString(1, registrationNumber)
            preparedStatement.setString(2, name)
            preparedStatement.setString(3, course)
            preparedStatement.setString(4, email)
            preparedStatement.setString(5, password)
            preparedStatement.setBytes(6, profilePictureBytes)
            preparedStatement.setBoolean(7, updatedUserModel.isAdministrator)
        }

        preparedStatement.execute()
    }

    fun deleteUser(registrationNumber: String) {
        database.executeStatement(
            "DELETE FROM usuario WHERE matricula = '${registrationNumber}'"
        )
    }

    fun isUserAdministrator(registrationNumber: String): Boolean {
        val queryResult = database.executeQuery(
            "SELECT eh_administrador FROM usuario WHERE matricula = '$registrationNumber'"
        )

        queryResult.next()

        return queryResult.getBoolean("eh_administrador")
    }

    fun getUser(userRegistrationNumber: String): UserModel {
        val queryResult = database.executeQuery(
            "SELECT * FROM usuario WHERE matricula = '$userRegistrationNumber'"
        )

        queryResult.next()

        val profilePic = if (queryResult.getObject("foto_de_perfil") == null) {
            null
        } else {
            val profilePicBytes = queryResult.getBytes("foto_de_perfil")
            val bufferedProfilePicImage = ImageIO.read(profilePicBytes.inputStream())
            bufferedProfilePicImage.toComposeImageBitmap()
        }


        return UserModel(
            queryResult.getString("matricula"),
            queryResult.getString("nome"),
            queryResult.getString("curso"),
            queryResult.getString("email"),
            queryResult.getString("senha"),
            profilePic,
            queryResult.getBoolean("eh_administrador"),
        )
    }


    fun getUserPassword(registrationNumber: String): String {
        val queryResult = database.executeQuery(
            "SELECT senha FROM usuario WHERE matricula = '${registrationNumber}'"
        )

        queryResult.next()

        return queryResult.getString("senha")
    }

    fun isRegistrationNumberInUse(registrationNumber: String): Boolean {
        val queryResult = database.executeQuery(
            "SELECT * FROM usuario WHERE matricula = '${registrationNumber}'"
        )

        return queryResult.next()
    }

    fun isEmailInUse(email: String): Boolean {
        val queryResult = database.executeQuery(
            "SELECT * FROM usuario WHERE email = '${email}'"
        )

        return queryResult.next()
    }
}