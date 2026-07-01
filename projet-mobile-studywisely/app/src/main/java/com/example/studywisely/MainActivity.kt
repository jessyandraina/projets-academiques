package com.example.studywisely

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.room.*
import com.example.studywisely.model.data.RoutinesDatabase
import com.example.studywisely.ui.navigation.AppNavHost
import com.example.studywisely.ui.theme.StudyWiselyTheme
import com.example.studywisely.repository.RoutineRepository
import com.example.studywisely.viewmodel.add_edit_routine.AddEditRoutineViewModel
import com.example.studywisely.viewmodel.routines_list.ListRoutinesViewModel

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.studywisely.model.notifications.ReminderScheduler

class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            RoutinesDatabase::class.java,
            RoutinesDatabase.DATABASE_NAME
        ).build()
    }

    private val repository by lazy { RoutineRepository(db) }
    private val reminderScheduler by lazy { ReminderScheduler(applicationContext) }

    private val listRoutinesViewModel by lazy {
        ListRoutinesViewModel(repository, reminderScheduler)
    }

    private val addEditRoutineViewModel by lazy {
        AddEditRoutineViewModel(repository, reminderScheduler)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestNotificationPermissionIfNeeded()

        setContent {
            StudyWiselyTheme {
                AppNavHost(
                    listRoutinesViewModel = listRoutinesViewModel,
                    addEditRoutineViewModel = addEditRoutineViewModel
                )
            }
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }
}