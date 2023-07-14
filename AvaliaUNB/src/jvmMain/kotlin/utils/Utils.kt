package utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.graphics.toComposeImageBitmap
import utils.resources.Paths
import utils.schedule.ClassDaySchedule
import utils.schedule.ScheduleShifts
import utils.schedule.ClassScheduleDays
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO
import kotlin.random.Random

private const val MAX_COLOR_COMPONENT_VALUE = 256
private const val MIN_COLOR_COMPONENT_VALUE = 0

object Utils {
    fun getRandomColorWithTransparency(transparency: Int): Color {
        val transparencyValue = when {
            transparency < MIN_COLOR_COMPONENT_VALUE -> MIN_COLOR_COMPONENT_VALUE
            transparency > MAX_COLOR_COMPONENT_VALUE -> MAX_COLOR_COMPONENT_VALUE
            else -> transparency
        }

        val red = Random.nextInt(MAX_COLOR_COMPONENT_VALUE)
        val green = Random.nextInt(MAX_COLOR_COMPONENT_VALUE)
        val blue = Random.nextInt(MAX_COLOR_COMPONENT_VALUE)

        return Color(red, green, blue, transparencyValue)
    }

    fun decodeSchedule(schedule: String): List<ClassDaySchedule> {
        val classSchedule = mutableListOf<ClassDaySchedule>()

        val regexSpaces = Regex("\\s+")
        val scheduleShifts = schedule.replace(regexSpaces, " ").trim().split(" ")
        println(schedule.trim().count { char -> char == ' '})

        for (shift in scheduleShifts) {

            val (scheduleDays, scheduleCodes) = shift.split("M", "T", "N")
            val shiftCode = shift.find { it in "MTN" }

            for (dayNumber in scheduleDays) {
                classSchedule.add(ClassDaySchedule(
                    day = ClassScheduleDays.values()[dayNumber.digitToInt() - 2],
                    shift = ScheduleShifts.values().find { it.code == shiftCode }!!,
                    scheduleCodes = scheduleCodes.map { it.digitToInt() }
                ))
            }

        }

        return classSchedule.toList()
    }

    fun getDefaultProfilePictureBytes(): ByteArray {
        val defaultProfilePic = ImageIO.read(File(Paths.Images.PERSON)).toComposeImageBitmap().toAwtImage()
        val stream = ByteArrayOutputStream()
        ImageIO.write(defaultProfilePic, "png", stream)

        return stream.toByteArray()
    }
}