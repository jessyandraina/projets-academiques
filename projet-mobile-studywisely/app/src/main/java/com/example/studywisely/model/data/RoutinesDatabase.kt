package com.example.studywisely.model.data

import androidx.room.*

@Database(entities = [RoutineEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class RoutinesDatabase : RoomDatabase() {
    abstract val dao: RoutineDao

    companion object {
        const val DATABASE_NAME = "routines.db"
    }
}