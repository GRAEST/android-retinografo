package br.com.graest.retinografo.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object FormatTime {

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatExamTime(examDate: LocalDateTime): String {

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")

        return formatter.format(examDate)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateAge(birthDate: LocalDate?): String {
        val currentDate = LocalDate.now()
        return Period.between(birthDate, currentDate).years.toString()
    }
}