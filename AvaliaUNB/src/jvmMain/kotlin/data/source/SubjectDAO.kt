package data.source

import androidx.compose.ui.graphics.Color
import data.models.ClassModel
import data.models.SemesterModel
import data.models.SubjectModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubjectDAO @Inject constructor(
    private val database: DatabaseManager,
    private val classDAO: ClassDAO
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
                    allSubjectsQueryResult.getInt("id"),
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

    fun getSubjectClasses(subjectId: Int): List<ClassModel> {
        val subjectClassesQuery = database.executeQuery(
            "SELECT turma.*, semestre.*, disciplina.nome AS disc_nome, disciplina.codigo AS disc_cod, " +
            "departamento.cor AS dept_cor, departamento.nome AS dept_nome " +
            "FROM turma " +
            "INNER JOIN disciplina ON turma.id_disciplina = disciplina.id " +
            "INNER JOIN departamento " +
            "ON disciplina.codigo_departamento = departamento.codigo AND " +
            "disciplina.numero_semestre = departamento.numero_semestre AND " +
            "disciplina.ano_semestre = departamento.ano_semestre " +
            "INNER JOIN semestre " +
            "ON semestre.ano = disciplina.ano_semestre AND " +
            "semestre.numero_semestre = disciplina.numero_semestre " +
            "WHERE turma.id_disciplina = $subjectId;"
        )

        val subjectClasses = mutableListOf<ClassModel>()

        while (subjectClassesQuery.next()) {
            subjectClasses.add(
                ClassModel(
                    subjectClassesQuery.getInt("id"),
                    subjectClassesQuery.getString("disc_nome"),
                    subjectClassesQuery.getString("disc_cod"),
                    subjectClassesQuery.getString("dept_nome"),
                    subjectClassesQuery.getString("codigo_departamento"),
                    subjectClassesQuery.getString("codigo_turma"),
                    subjectClassesQuery.getString("horario"),
                    subjectClassesQuery.getInt("num_horas"),
                    subjectClassesQuery.getInt("vagas_ocupadas"),
                    subjectClassesQuery.getInt("vagas_total"),
                    subjectClassesQuery.getString("local_aula"),
                    subjectClassesQuery.getString("nome_professor"),
                    SemesterModel(
                        subjectClassesQuery.getInt("ano"),
                        subjectClassesQuery.getInt("numero_semestre"),
                        subjectClassesQuery.getString("data_inicio"),
                        subjectClassesQuery.getString("data_fim"),
                    ),
                    Color(subjectClassesQuery.getInt("dept_cor")),
                    subjectClassesQuery.getObject("pontuacao") as Double?,
                    classDAO.getClassReviews(subjectClassesQuery.getInt("id")).size
                )
            )
        }

        return subjectClasses.toList()

    }
}