package data.source

import androidx.compose.ui.graphics.toComposeImageBitmap
import data.models.UserModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files
import javax.imageio.ImageIO
import javax.inject.Inject
import javax.inject.Singleton

private const val DEFAULT_PROFILE_PIC_PATH = "src/jvmMain/resources/images/person.png"

@Singleton
class UserDAO @Inject constructor(
    private val database: DatabaseManager
) {

    fun insertUser(user: UserModel) {
        val defaultProfilePicture = File(DEFAULT_PROFILE_PIC_PATH)
        val svgBytes = Files.readAllBytes(defaultProfilePicture.toPath())

        database.executeStatement(
            "INSERT INTO avalia_unb.usuario VALUES ('%s', '%s', '%s', '%s', '%s', '%s');".format(
                user.registrationNumber,
                user.name,
                user.course,
                user.email,
                user.password,
                svgBytes
            )
        )
    }

    fun getUser(userRegistrationNumber: String): UserModel {
        val queryResult = database.executeQuery(
            "SELECT * FROM avalia_unb.usuario WHERE matricula = '${userRegistrationNumber}'"
        )

        queryResult.next()


        val profilePicBytes = queryResult.getBytes("foto_de_perfil")

        println(profilePicBytes.inputStream())
        val bufferedImage = ImageIO.read(profilePicBytes.inputStream())

        //val imageBitmap = bufferedImage.toComposeImageBitmap()

        return UserModel(
            queryResult.getString("matricula"),
            queryResult.getString("nome"),
            queryResult.getString("curso"),
            queryResult.getString("email"),
            queryResult.getString("senha"),
            //imageBitmap
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