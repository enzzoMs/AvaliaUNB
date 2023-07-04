package data.source

import androidx.compose.ui.graphics.Color
import data.models.SubjectModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubjectDAO @Inject constructor(
    private val database: DatabaseManager
) {
    fun getAllSubjects(): List<SubjectModel> {
        val queryResult = database.executeQuery(
            "SELECT disc.*, dept.nome AS dept_nome, dept.cor AS dept_cor " +
                    "FROM avalia_unb.disciplina as disc " +
                    "INNER JOIN avalia_unb.departamento as dept " +
                    "ON disc.codigo_departamento = dept.codigo " +
                    "AND disc.ano_semestre = dept.ano_semestre " +
                    "AND disc.numero_semestre = dept.numero_semestre;"
        )

        val subjects = mutableListOf<SubjectModel>()

        while (queryResult.next()) {
            val subjectSemesterYear = queryResult.getString("ano_semestre")
            val subjectSemesterNumber = queryResult.getString("numero_semestre")

            val subjectSemester = "${subjectSemesterYear}-${subjectSemesterNumber}"

            subjects.add(
                SubjectModel(
                    queryResult.getString("codigo"),
                    queryResult.getString("nome"),
                    subjectSemester,
                    queryResult.getString("dept_nome"),
                    Color(queryResult.getInt("dept_cor"))
                )
            )
        }

        return subjects.toList()
    }
}