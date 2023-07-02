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
            "SELECT disc.*, dept.nome as dept_nome, dept.cor as dept_cor " +
                    "FROM avalia_unb.disciplina as disc " +
                    "INNER JOIN avalia_unb.departamento as dept " +
                    "on disc.codigo_departamento = dept.codigo AND disc.semestre = dept.semestre;"
        )

        val subjects = mutableListOf<SubjectModel>()

        while (queryResult.next()) {
            subjects.add(
                SubjectModel(
                    queryResult.getString("codigo"),
                    queryResult.getString("nome"),
                    queryResult.getString("semestre"),
                    queryResult.getString("dept_nome"),
                    Color(queryResult.getInt("dept_cor"))
                )
            )
        }

        return subjects.toList()
    }
}