package com.example.buildyourself.persistence

import androidx.room.Dao
import androidx.room.*

@Dao
interface TaskDao {
    @Query("SELECT * FROM Task")
    suspend fun getAll(): List<Task>

    @Insert
    suspend fun insert(task: Task)

    @Delete
    suspend fun delete(task: Task)
}