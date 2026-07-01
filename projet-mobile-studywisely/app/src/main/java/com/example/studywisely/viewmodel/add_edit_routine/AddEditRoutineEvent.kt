package com.example.studywisely.viewmodel.add_edit_routine

sealed class AddEditRoutineEvent {

    data class EnteredTitle(val value: String) : AddEditRoutineEvent()

    data class EnteredDescription(val value: String) : AddEditRoutineEvent()

    data class PickedRoutineDateTime(val millis: Long?) : AddEditRoutineEvent()

    data class PickedExamDateTime(val millis: Long?) : AddEditRoutineEvent()
    data class PickedLocation(
        val latitude: Double,
        val longitude: Double
    ) : AddEditRoutineEvent()
    object SaveRoutine : AddEditRoutineEvent()
}