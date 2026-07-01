package com.example.studywisely.model.data

import androidx.room.TypeConverter
import com.example.studywisely.model.local.PriorityType

class Converters {

    @TypeConverter
    fun fromPriority(priority: PriorityType?): String? {
        return priority?.label
    }

    @TypeConverter
    fun toPriority(value: String?): PriorityType? {
        return when (value) {
            "Faible" -> PriorityType.Faible
            "Moyenne" -> PriorityType.Moyenne
            "Elevee" -> PriorityType.Elevee
            else -> PriorityType.Moyenne
        }
    }
}