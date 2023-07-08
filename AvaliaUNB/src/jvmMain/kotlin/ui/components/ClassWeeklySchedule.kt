package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import theme.LightGray
import theme.UnbBlue
import theme.White
import utils.Utils
import utils.resources.ResourcesUtils
import utils.schedule.ScheduleShifts
import utils.schedule.ClassScheduleDays

private val SCHEDULE_MORNING_CODES = 1..5
private val SCHEDULE_AFTERNOON_CODES = 1..7
private val SCHEDULE_NIGHT_CODES = 1..4

@Composable
fun ClassWeeklySchedule(
    schedule: String,
    classSubjectCode: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .padding(top = 8.dp)
            .clip(RoundedCornerShape(10.dp))
            .then(modifier)
    ) {
        Row {
            ScheduleColumn(modifier = Modifier.weight(1f))

            val classDaySchedules = Utils.decodeSchedule(schedule)

            for (day in ClassScheduleDays.values()) {
                ScheduleWeekDay(
                    day = day,
                    classSubjectCode = classSubjectCode,
                    classHaveLectureOnDay = { classDay: ClassScheduleDays, shift: ScheduleShifts, scheduleCode: Int ->
                        classDaySchedules.find { it.day == classDay && it.shift == shift }?.scheduleCodes?.contains(scheduleCode) ?: false
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ScheduleColumn(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = ResourcesUtils.Strings.SCHEDULES,
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Center,
            color = White,
            modifier = Modifier
                .fillMaxWidth()
                .background(UnbBlue)
                .padding(vertical = 8.dp, horizontal = 20.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(LightGray)
        ) {
            for (scheduleTime in ResourcesUtils.Strings.SCHEDULES_LIST) {
                Text(
                    text = scheduleTime,
                    style = MaterialTheme.typography.subtitle1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 10.dp)
                )
            }
        }

    }
}

@Composable
private fun ScheduleWeekDay(
    day: ClassScheduleDays,
    classSubjectCode: String,
    classHaveLectureOnDay: (ClassScheduleDays, ScheduleShifts, Int) -> Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = day.label,
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Center,
            color = White,
            modifier = Modifier
                .fillMaxWidth()
                .background(UnbBlue)
                .padding(vertical = 8.dp, horizontal = 10.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(White)
        ) {
            val scheduleCodesByShift = listOf(
                SCHEDULE_MORNING_CODES to ScheduleShifts.MORNING,
                SCHEDULE_AFTERNOON_CODES to ScheduleShifts.AFTERNOON,
                SCHEDULE_NIGHT_CODES to ScheduleShifts.NIGHT
            )

            for ((scheduleShiftCodes, shift) in scheduleCodesByShift) {
                for (scheduleCode in scheduleShiftCodes) {
                    Text(
                        text = if (classHaveLectureOnDay(day, shift, scheduleCode)) classSubjectCode else "--------",
                        style = MaterialTheme.typography.subtitle1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 10.dp)
                    )
                }
            }
        }
    }
}






