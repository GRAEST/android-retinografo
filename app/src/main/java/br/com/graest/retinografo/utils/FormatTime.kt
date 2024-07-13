package br.com.graest.retinografo.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object FormatTime {

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatExamTime(examTime: Long): String {
        // Convert milliseconds to LocalDateTime
        val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(examTime), ZoneId.systemDefault())

        // Define the date and time format
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")

        // Format LocalDateTime using the formatter
        return formatter.format(localDateTime)
    }
}