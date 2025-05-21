package com.example.mobile6.presentation.ui.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.sql.Date as SqlDate

object DateUtils {

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val isoDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    private val ddMMyyyyFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val dateTimeFormatter = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault())
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    // Convert java.util.Date to java.sql.Date
    fun Date.toSqlDate(): SqlDate = SqlDate(this.time)

    // Convert java.sql.Date to java.util.Date
    fun SqlDate.toUtilDate(): Date = Date(this.time)

    // Convert java.util.Date to string "yyyy-MM-dd"
    fun Date.toRequestDateString(): String = dateFormatter.format(this)

    fun Date.toddMMyyyyString(): String = ddMMyyyyFormatter.format(this)

    fun String.toDateTimeString(): String {
        return try {
            // Try parsing with standard ISO format
            val parsedDate = isoDateTimeFormatter.parse(this)
            dateTimeFormatter.format(parsedDate)
        } catch (e: Exception) {
            try {
                // Fallback parsing if the input has milliseconds or different timezone
                val alternateFormatter = DateTimeFormatter.ISO_DATE_TIME
                val dateTime = LocalDateTime.parse(this, alternateFormatter)
                dateTimeFormatter.format(
                    Date.from(
                        dateTime.atZone(ZoneId.systemDefault()).toInstant()
                    )
                )
            } catch (e: Exception) {
                // Return original string if parsing fails
                this
            }
        }
    }

    // Convert java.util.Date to ISO datetime string "yyyy-MM-dd'T'HH:mm:ss"
    fun Date.toRequestDateTimeString(): String {
        val localDateTime: LocalDateTime = this.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
        return localDateTime.format(isoDateTimeFormatter)
    }

    fun LocalTime.toRequestTimeString(): String {
        return this.format(timeFormatter)
    }

    // Helper: Convert java.util.Date to java.time.LocalDate
    fun Date.toLocalDate(): LocalDate =
        this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

    fun String.toUtilDate(): Date = dateFormatter.parse(this)!!

    // Convert LocalDateTime to string
    fun LocalDateTime.toRequestDateTimeString(): String =
        this.format(isoDateTimeFormatter)
}
