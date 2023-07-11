package data.repositories

import data.models.TeacherModel
import data.models.TeacherReviewModel
import data.source.TeacherDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeacherRepository @Inject constructor(
    private val teacherDAO: TeacherDAO
) {
    suspend fun getTeacherReviews(teacherModel: TeacherModel): List<TeacherReviewModel> {
        val allReviewsDeferred = CoroutineScope(Dispatchers.IO).async {
            teacherDAO.getTeacherReviews(
                teacherModel.name,
                teacherModel.departmentCode
            )
        }
        return allReviewsDeferred.await()
    }

    fun getTeacherScore(teacherName: String, departmentCode: Int): Double? {
        return teacherDAO.getTeacherScore(teacherName, departmentCode)
    }
}
