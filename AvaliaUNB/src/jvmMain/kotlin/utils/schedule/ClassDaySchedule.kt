package utils.schedule

data class ClassDaySchedule(
    val day: ClassScheduleDays,
    val shift: ScheduleShifts,
    val scheduleCodes: List<Int>
)
