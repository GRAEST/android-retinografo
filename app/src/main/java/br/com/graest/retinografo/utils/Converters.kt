package br.com.graest.retinografo.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import br.com.graest.retinografo.data.model.Gender
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class Converters {
    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let {
            LocalDate.parse(it, formatter)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime): Long {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDateTime(timestamp: Long): LocalDateTime {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    @TypeConverter
    fun fromGender(value: Gender): String {
        return value.name
    }

    @TypeConverter
    fun toGender(value: String): Gender {
        return Gender.valueOf(value)
    }

    @TypeConverter
    fun fromImagePathList(imagePaths: List<String>): String {
        return Gson().toJson(imagePaths)
    }

    @TypeConverter
    fun toImagePathList(imagePathString: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(imagePathString, listType)
    }


    @TypeConverter
    fun fromBinaryList(binaryList: List<ByteArray>): String {
        return binaryList.joinToString(separator = ";") { it.joinToString(separator = ",") { byte -> byte.toString() } }
    }

    @TypeConverter
    fun toBinaryList(data: String): List<ByteArray> {
        return data.split(";").mapNotNull {
            val byteList = it.split(",").mapNotNull { str ->
                str.toByteOrNull()
            }
            if (byteList.isNotEmpty()) byteList.toByteArray() else null
        }
    }
    // Converter for LocalDate
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromLocalDateSystem(date: LocalDate): Long {
        return date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDateSystem(timestamp: Long): LocalDate {
        return LocalDate.ofEpochDay(timestamp / (24 * 60 * 60 * 1000))
    }

}