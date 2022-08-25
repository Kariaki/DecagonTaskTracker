package com.thirdwinter.tasktracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.thirdwinter.tasktracker.database.dao.TaskDao
import com.thirdwinter.tasktracker.model.Task

@Database(
    entities = [Task::class],
    exportSchema = false,
    version = 2
)
abstract class TaskTrackerDatabase :RoomDatabase(){

    abstract fun taskDao(): TaskDao

}