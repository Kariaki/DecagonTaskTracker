package com.thirdwinter.tasktracker.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.thirdwinter.tasktracker.database.TaskTrackerDatabase
import com.thirdwinter.tasktracker.database.dao.TaskDao
import com.thirdwinter.tasktracker.getOrAwaitValue
import com.thirdwinter.tasktracker.model.Task
import com.thirdwinter.tasktracker.repository.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.google.common.truth.Truth.assertThat
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TaskViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var dao: TaskDao
    private lateinit var database: TaskTrackerDatabase
    private lateinit var repository: TaskRepository
    private lateinit var viewModel: TaskViewModel

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TaskTrackerDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        dao = database.taskDao()
        repository = TaskRepository(dao)
        viewModel = TaskViewModel(repository)
    }

    @Test
    fun testInsertTask() = runBlocking {
        val task = Task(
            title = "Contact the CEO of Decagon",
            completed = false
        )
        viewModel.insertTask(task)
        //viewModel.getTask()
        val checkResult = viewModel.allTask.getOrAwaitValue(time = 200, timeUnit = TimeUnit.SECONDS)
        assertThat(checkResult).isEmpty()
    }

    @Test
    fun testDeleteTask() = runBlocking {
        val task = Task(
            title = "Contact the CEO of Decagon",
            completed = false
        )
        viewModel.insertTask(task)

        viewModel.deleteTask(task)
        val checkResult = viewModel.allTask.getOrAwaitValue(time = 200, timeUnit = TimeUnit.SECONDS)

        assertThat(checkResult).isEmpty()
    }

    @Test
    fun taskGetTaskById() = runBlocking {
        val task = Task(
            title = "Contact the CEO of Decagon",
            completed = false
        )
        viewModel.insertTask(task)
        val id = task.id

        // val newTask = viewModel.getTaskById(id)
        viewModel.getCurrentTask(id)
        val newTask = viewModel.currentTask.getOrAwaitValue()
        assertThat(newTask).isNotNull()

    }


}