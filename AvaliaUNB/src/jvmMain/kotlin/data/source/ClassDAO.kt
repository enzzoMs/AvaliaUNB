package data.source

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toComposeImageBitmap
import data.models.ClassModel
import data.models.SemesterModel
import data.models.TeacherModel
import javax.imageio.ImageIO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassDAO @Inject constructor(
    private val database: DatabaseManager,
    private val reviewDAO: ReviewDAO
) {

    fun getAllClasses(): List<ClassModel> {
        val allClassesQuery = database.executeQuery(
            "SELECT * FROM TURMAS_INFORMACOES"
        )

        val classes = mutableListOf<ClassModel>()

        while (allClassesQuery.next()) {
            classes.add(
                ClassModel(
                    allClassesQuery.getInt("id"),
                    allClassesQuery.getString("disc_nome"),
                    allClassesQuery.getString("disc_cod"),
                    allClassesQuery.getString("dept_nome"),
                    allClassesQuery.getString("codigo_departamento"),
                    allClassesQuery.getString("codigo_turma"),
                    allClassesQuery.getString("horario"),
                    allClassesQuery.getInt("num_horas"),
                    allClassesQuery.getInt("vagas_ocupadas"),
                    allClassesQuery.getInt("vagas_total"),
                    allClassesQuery.getString("local_aula"),
                    allClassesQuery.getString("nome_professor"),
                    SemesterModel(
                        allClassesQuery.getInt("ano"),
                        allClassesQuery.getInt("numero_semestre"),
                        allClassesQuery.getString("data_inicio"),
                        allClassesQuery.getString("data_fim"),
                    ),
                    Color(allClassesQuery.getInt("dept_cor")),
                    allClassesQuery.getObject("pontuacao") as Double?,
                    reviewDAO.getClassReviews(allClassesQuery.getInt("id")).size
                )
            )
        }

        return classes.toList()
    }

    fun getClassTeacher(classModel: ClassModel): TeacherModel {
        val classTeacherQueryResult = database.executeQuery(
            "SELECT * FROM PROFESSORES_INFORMACOES " +
                    "WHERE nome = '${classModel.teacherName}' AND " +
                    "codigo_departamento = '${classModel.departmentCode}';"
        )

        classTeacherQueryResult.next()

        val profilePic = if (classTeacherQueryResult.getObject("foto_de_perfil") == null) {
            null
        } else {
            val profilePicBytes = classTeacherQueryResult.getBytes("foto_de_perfil")
            val bufferedProfilePicImage = ImageIO.read(profilePicBytes.inputStream())
            bufferedProfilePicImage.toComposeImageBitmap()
        }

        val teacherSemesterYear = classTeacherQueryResult.getString("ano_semestre")
        val teacherSemesterNumber = classTeacherQueryResult.getString("numero_semestre")

        val teacherSemester = "${teacherSemesterYear}-${teacherSemesterNumber}"

        return TeacherModel(
            classTeacherQueryResult.getString("nome"),
            classTeacherQueryResult.getString("dept_nome"),
            classTeacherQueryResult.getInt("codigo_departamento"),
            teacherSemester,
            classTeacherQueryResult.getObject("pontuacao") as Double?,
            classTeacherQueryResult.getInt("num_avaliacoes"),
            profilePic
        )
    }

    fun getClassScore(classId: Int): Double? {
        val scoreQueryResult = database.executeQuery(
            "SELECT pontuacao FROM turma " +
                    "WHERE id = $classId"
        )

        scoreQueryResult.next()

        return scoreQueryResult.getObject("pontuacao") as Double?
    }
}