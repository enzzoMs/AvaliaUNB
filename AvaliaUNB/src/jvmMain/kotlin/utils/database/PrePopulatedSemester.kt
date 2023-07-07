package utils.database

enum class PrePopulatedSemester(
    val year: Int,
    val semester_number: Int,
    val begin_date: String,
    val end_date: String
) {
    PRE_POPULATED_SEMESTER_2022_1(2022, 1, "2022-02-15", "2022-06-03"),
    PRE_POPULATED_SEMESTER_2022_2(2022, 2, "2022-08-01", "2022-11-19"),
    PRE_POPULATED_SEMESTER_2023_1(2023, 1, "2023-02-13", "2023-07-02")
}