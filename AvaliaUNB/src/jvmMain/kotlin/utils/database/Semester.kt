package utils.database

import java.sql.Date

enum class Semester(
    val year: Int,
    val semester_number: Int,
    val begin_date: Date,
    val end_date: Date
) {
    SEMESTER_2022_1(2022, 1, Date.valueOf("2022-2-15"), Date.valueOf("2022-06-03")),
    SEMESTER_2022_2(2022, 2, Date.valueOf("2022-08-01"), Date.valueOf("2022-11-19")),
    SEMESTER_2023_1(2023, 1, Date.valueOf("2023-02-13"), Date.valueOf("2023-07-02")),
}