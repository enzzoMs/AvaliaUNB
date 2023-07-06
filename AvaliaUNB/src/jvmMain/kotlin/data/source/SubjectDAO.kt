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
        val allSubjectsQueryResult = database.executeQuery(
            "SELECT disc.*, dept.nome AS dept_nome, dept.cor AS dept_cor " +
                    "FROM disciplina as disc " +
                    "INNER JOIN departamento as dept " +
                    "ON disc.codigo_departamento = dept.codigo " +
                    "AND disc.ano_semestre = dept.ano_semestre " +
                    "AND disc.numero_semestre = dept.numero_semestre;"
        )

        val numberOfClassesQuery = database.prepareStatement(
            "SELECT id_disciplina, COUNT(*) AS total_turmas " +
                    "FROM turma " +
                    "GROUP BY id_disciplina;"
        ).executeQuery()

        val subjectNumberOfClassesMap = mutableMapOf<Int, Int>()

        while (numberOfClassesQuery.next()) {
            val subjectId = numberOfClassesQuery.getInt("id_disciplina")
            val numberOfClasses = numberOfClassesQuery.getInt("total_turmas")
            subjectNumberOfClassesMap[subjectId] = numberOfClasses
        }

        val subjects = mutableListOf<SubjectModel>()

        while (allSubjectsQueryResult.next()) {
            val subjectSemesterYear = allSubjectsQueryResult.getString("ano_semestre")
            val subjectSemesterNumber = allSubjectsQueryResult.getString("numero_semestre")

            val subjectSemester = "${subjectSemesterYear}-${subjectSemesterNumber}"

            subjects.add(
                SubjectModel(
                    allSubjectsQueryResult.getString("codigo"),
                    allSubjectsQueryResult.getString("nome"),
                    subjectSemester,
                    allSubjectsQueryResult.getString("dept_nome"),
                    Color(allSubjectsQueryResult.getInt("dept_cor")),
                    subjectNumberOfClassesMap[allSubjectsQueryResult.getInt("id")] ?: 0
                )
            )
        }

        return subjects.toList()
    }
}