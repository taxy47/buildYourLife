package com.example.buildyourself.persistence

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}

// 还是 mvc 架构，非常重复化，可以做成模板，比较考验熟悉程度和项目常见架构
