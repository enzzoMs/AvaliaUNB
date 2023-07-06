package data.source

import data.models.SemesterModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SemesterDAO @Inject constructor(
    private val database: DatabaseManager
) {

    fun getAllSemesters(): List<SemesterModel> {
        val allSemestersQueryResult = database.executeQuery(
            "SELECT * FROM semestre;"
        )

        val semesters = mutableListOf<SemesterModel>()

        while (allSemestersQueryResult.next()) {
            semesters.add(
                SemesterModel(
                    allSemestersQueryResult.getString("ano").toInt(),
                    allSemestersQueryResult.getString("numero_semestre").toInt(),
                    allSemestersQueryResult.getString("data_inicio"),
                    allSemestersQueryResult.getString("data_fim")
                )
            )
        }

        return semesters.toList()
    }
}