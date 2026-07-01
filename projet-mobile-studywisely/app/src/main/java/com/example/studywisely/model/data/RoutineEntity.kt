package com.example.studywisely.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.studywisely.model.local.PriorityType

@Entity(tableName = "routines")
data class RoutineEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String,
    val description: String? = null,
    val routineDateTimeMillis: Long? = null,
    val examDateTimeMillis: Long? = null,
    val priority: PriorityType = PriorityType.Moyenne,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)