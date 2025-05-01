package com.achmadichzan.dicodingstory.presentation.util

import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

fun formatIndonesianDate(isoTime: String): String {
    val localeID = Locale("id", "ID")
    val inputFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
    val outputFormatter = DateTimeFormatter.ofPattern("EEE, d MMM yy h:mm a", localeID)
    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a", localeID)

    val dateTime = ZonedDateTime.parse(isoTime, inputFormatter)
        .withZoneSameInstant(ZoneId.systemDefault())

    val now = ZonedDateTime.now(ZoneId.systemDefault())
    val yesterday = now.minusDays(1)

    return if (dateTime.toLocalDate() == yesterday.toLocalDate()) {
        "Kemarin ${dateTime.format(timeFormatter)}"
    } else {
        dateTime.format(outputFormatter)
    }
}