package data.repositories

import data.models.SubjectModel
import data.source.SubjectDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubjectRepository @Inject constructor(
    private val subjectDAO: SubjectDAO
){

    suspend fun getAllSubjects(): List<SubjectModel> {
        val allSubjectsDeferred = CoroutineScope(Dispatchers.IO).async {
            subjectDAO.getAllSubjects()
        }
        return allSubjectsDeferred.await()
    }
}