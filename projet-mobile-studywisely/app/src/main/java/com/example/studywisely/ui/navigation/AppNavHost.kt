package com.example.studywisely.ui.navigation
import com.example.studywisely.ui.screens.MapPickerScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.studywisely.ui.screens.AddEditRoutineScreen
import com.example.studywisely.ui.screens.RoutinesListScreen
import com.example.studywisely.viewmodel.add_edit_routine.AddEditRoutineViewModel
import com.example.studywisely.viewmodel.routines_list.ListRoutinesViewModel

@Composable
fun AppNavHost(listRoutinesViewModel: ListRoutinesViewModel,
               addEditRoutineViewModel: AddEditRoutineViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.RoutinesListScreen.route
    ) {
        composable(Screen.RoutinesListScreen.route) {
            RoutinesListScreen(navController = navController, viewModel = listRoutinesViewModel)
        }

        composable(
            route = "${Screen.AddEditRoutineScreen.route}?routineId={routineId}",
            arguments = listOf(
                navArgument("routineId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            AddEditRoutineScreen(navController = navController, viewModel = addEditRoutineViewModel)
        }
        composable(Screen.MapPickerScreen.route) {
    MapPickerScreen(navController = navController)
}
    }
}
