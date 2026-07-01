package com.example.studywisely.model.local

import com.example.studywisely.model.data.RoutineEntity

data class RoutineModel(
    val id: Int? = null,
    val title: String = "",
    val description: String = "",

    // Date/heure de la routine (révision)
    val routineDateTimeMillis: Long? = null,

    // Date/heure de l'examen ou livrable prévu
    val examDateTimeMillis: Long? = null,

    // Priorité (calculée selon examDateTimeMillis, mais stockée aussi)
    val priority: PriorityType = PriorityType.Moyenne,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) {
    companion object{
        fun fromEntity(entity: RoutineEntity): RoutineModel {
            return RoutineModel(
                id = entity.id,
                title = entity.title,
                description = entity.description ?: "",
                routineDateTimeMillis = entity.routineDateTimeMillis,
                examDateTimeMillis = entity.examDateTimeMillis,
                priority = entity.priority,
                        latitude = entity.latitude,
                longitude = entity.longitude
            )
        }

        fun toEntity(vm: RoutineModel): RoutineEntity {
            return RoutineEntity(
                id = vm.id,
                title = vm.title,
                description = vm.description.ifEmpty { null },
                routineDateTimeMillis = vm.routineDateTimeMillis,
                examDateTimeMillis = vm.examDateTimeMillis,
                priority = vm.priority,
                latitude = vm.latitude,
                longitude = vm.longitude
            )
        }
    }
}