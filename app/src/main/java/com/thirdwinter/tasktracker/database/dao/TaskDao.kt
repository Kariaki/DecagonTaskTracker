package com.thirdwinter.tasktracker.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.thirdwinter.tasktracker.model.Task

@Dao
interface TaskDao {

    @Insert
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("Select * from task order by timeStamp desc")
    fun getTaskByDescendingSort(): LiveData<List<Task>>

    @Query("Select * from task order by timeStamp ")
    fun getTaskByAscendingSort(): LiveData<List<Task>>

    @Query("Select * from task where id like:id")
    suspend fun getTaskById(id: String): Task?


}