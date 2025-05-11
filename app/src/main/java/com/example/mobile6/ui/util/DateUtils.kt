package com.example.mobile6.ui.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.sql.Date as SqlDate

object DateUtils {

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val isoDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    private val ddMMyyyyFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // Convert java.util.Date to java.sql.Date
    fun Date.toSqlDate(): SqlDate = SqlDate(this.time)

    // Convert java.sql.Date to java.util.Date
    fun SqlDate.toUtilDate(): Date = Date(this.time)

    // Convert java.util.Date to string "yyyy-MM-dd"
    fun Date.toRequestDateString(): String = dateFormatter.format(this)

    fun Date.toddMMyyyyString(): String = ddMMyyyyFormatter.format(this)

    // Convert java.util.Date to ISO datetime string "yyyy-MM-dd'T'HH:mm:ss"
    fun Date.toRequestDateTimeString(): String {
        val localDateTime: LocalDateTime = this.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
        return localDateTime.format(isoDateTimeFormatter)
    }

    // Helper: Convert java.util.Date to java.time.LocalDate
    fun Date.toLocalDate(): LocalDate =
        this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

    fun String.toUtilDate(): Date = DateUtils.dateFormatter.parse(this)!!

    fun String.toLocalDateTime(): LocalDateTime =
        LocalDateTime.parse(this, DateUtils.isoDateTimeFormatter)

    // Convert LocalDateTime to string
    fun LocalDateTime.toRequestDateTimeString(): String =
        this.format(isoDateTimeFormatter)
}
