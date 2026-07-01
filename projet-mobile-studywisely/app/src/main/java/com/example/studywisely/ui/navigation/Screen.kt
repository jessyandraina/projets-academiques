package com.example.studywisely.ui.navigation

sealed class Screen(val route: String) {
    data object RoutinesListScreen : Screen("routines_list_screen")
    data object AddEditRoutineScreen : Screen("add_edit_routine")
    data object MapPickerScreen : Screen("map_picker")

    fun withRoutineId(routineId: Int): String {
        return "${AddEditRoutineScreen.route}?routineId=$routineId"
    }
}
