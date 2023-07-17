package data.repositories

import data.models.TeacherModel
import data.models.TeacherReviewModel
import data.source.ReviewDAO
import data.source.TeacherDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeacherRepository @Inject constructor(
    private val teacherDAO: TeacherDAO,
    private val reviewDAO: ReviewDAO
) {
    suspend fun getTeacherReviews(teacherModel: TeacherModel): List<TeacherReviewModel> {
        val allReviewsDeferred = CoroutineScope(Dispatchers.IO).async {
            reviewDAO.getTeacherReviews(
                teacherModel.name,
                teacherModel.departmentCode
            )
        }
        return allReviewsDeferred.await()
    }

    suspend fun getAllTeachers(): List<TeacherModel> {
        val allTeachersDeferred = CoroutineScope(Dispatchers.IO).async {
            teacherDAO.getAllTeachers()
        }
        return allTeachersDeferred.await()
    }

    fun getTeacherScore(teacherName: String, departmentCode: Int): Double? {
        return teacherDAO.getTeacherScore(teacherName, departmentCode)
    }
}
