package data.repositories

import data.models.SemesterModel
import data.source.SemesterDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SemesterRepository @Inject constructor(
    private val semesterDAO: SemesterDAO
) {

    fun getAllSemesters(): List<SemesterModel> = semesterDAO.getAllSemesters()
}