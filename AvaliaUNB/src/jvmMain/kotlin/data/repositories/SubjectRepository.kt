package data.repositories

import data.models.SubjectModel
import data.source.SubjectDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubjectRepository @Inject constructor(
    private val subjectDAO: SubjectDAO
){

    fun getAllSubjects(): List<SubjectModel> = subjectDAO.getAllSubjects()
}