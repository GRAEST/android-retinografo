package br.com.graest.retinografo.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import br.com.graest.retinografo.data.model.Gender
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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
}