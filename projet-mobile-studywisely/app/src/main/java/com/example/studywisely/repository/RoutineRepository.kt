package com.example.studywisely.repository

import com.example.studywisely.model.data.RoutinesDatabase
import com.example.studywisely.model.local.RoutineModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoutineRepository(private val db: RoutinesDatabase) {
    fun getAllRoutines(): Flow<List<RoutineModel>> {
        return db.dao.getAllRoutines().map { list ->
            list.map { RoutineModel.Companion.fromEntity(it) }
        }
    }

    suspend fun getRoutineById(id: Int): RoutineModel? {
        return db.dao.getRoutineById(id)?.let { RoutineModel.Companion.fromEntity(it) }
    }

    suspend fun insertRoutine(routine: RoutineModel): Int {
        return db.dao.insert(RoutineModel.Companion.toEntity(routine)).toInt()
    }

    suspend fun updateRoutine(routine: RoutineModel) {
        db.dao.update(RoutineModel.Companion.toEntity(routine))
    }

    suspend fun deleteRoutine(id: Int) {
        db.dao.deleteById(id)
    }
}