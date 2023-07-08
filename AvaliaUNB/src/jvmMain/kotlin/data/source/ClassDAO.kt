package data.source

import androidx.compose.ui.graphics.toComposeImageBitmap
import data.models.ClassModel
import data.models.TeacherModel
import javax.imageio.ImageIO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassDAO @Inject constructor(
    private val database: DatabaseManager
) {
    fun getClassTeacher(classModel: ClassModel): TeacherModel {
        val classTeacherQueryResult = database.executeQuery(
            "SELECT * FROM professor " +
                    "WHERE nome = '${classModel.teacherName}' AND codigo_departamento = '${classModel.departmentCode}';"
        )

        classTeacherQueryResult.next()

        val profilePicBytes = classTeacherQueryResult.getBytes("foto_de_perfil")
        val bufferedProfilePicImage = ImageIO.read(profilePicBytes.inputStream())
        val profilePic = bufferedProfilePicImage.toComposeImageBitmap()

        return TeacherModel(
            classTeacherQueryResult.getString("nome"),
            classTeacherQueryResult.getObject("pontuacao") as Int?,
            profilePic
        )
    }
}