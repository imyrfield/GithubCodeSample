package com.ianmyrfield.githubcodesample.ui.details

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {

    fun formatDate(date: String?): String? {
        date ?: return null

        val localDate = LocalDate.parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val outputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyy", Locale.getDefault())

        return outputFormatter.format(localDate)
    }
}