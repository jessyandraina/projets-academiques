package com.example.studywisely.model.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Query("SELECT * FROM routines ORDER BY " +
            "CASE priority " +
            "   WHEN 'Elevee' THEN 0 " +
            "   WHEN 'Moyenne' THEN 1 " +
            "   WHEN 'Faible' THEN 2 " +
            "END, " +
            "examDateTimeMillis ASC, " +
            "routineDateTimeMillis ASC")
    fun getAllRoutines(): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM routines WHERE id = :id")
    suspend fun getRoutineById(id: Int): RoutineEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(routine: RoutineEntity): Long

    @Update
    suspend fun update(routine: RoutineEntity)

    @Query("DELETE FROM routines WHERE id = :id")
    suspend fun deleteById(id: Int)
}