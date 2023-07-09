package data.repositories

import data.models.ClassModel
import data.models.ClassReviewModel
import data.models.TeacherModel
import data.source.ClassDAO
import data.source.SubjectDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassRepository @Inject constructor(
    private val classDAO: ClassDAO
) {

    fun getClassTeacher(classModel: ClassModel): TeacherModel = classDAO.getClassTeacher(classModel)

    fun getClassReviews(classModel: ClassModel): List<ClassReviewModel> = classDAO.getClassReviews(classModel)
}