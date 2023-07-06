package utils.database

data class DatabaseConfiguration (
    val initializeDatabase: Boolean,
    val loadDataForSemester2022_1: Boolean,
    val loadDataForSemester2022_2: Boolean,
    val loadDataForSemester2023_1: Boolean
)