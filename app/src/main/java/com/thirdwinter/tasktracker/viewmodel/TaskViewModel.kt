package com.thirdwinter.tasktracker.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thirdwinter.tasktracker.model.Task
import com.thirdwinter.tasktracker.repository.TaskRepository
import com.thirdwinter.tasktracker.ui.Filter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {

    //Stack
    private val _undoStack: Stack<String> = Stack()
    val undoStack: Stack<String> = _undoStack

    private val _editing:MutableLiveData<Boolean> = MutableLiveData(true)
    val editing:LiveData<Boolean> = _editing

    private val _descending: MutableLiveData<Boolean> = MutableLiveData(false)
    val descending: LiveData<Boolean> = _descending
    private val _currentTask: MutableLiveData<Task> = MutableLiveData(null)
    val currentTask = _currentTask

    fun insertTask(task: Task) {
        viewModelScope.launch {
            val findTask = getTaskById(task.id) ?: kotlin.run {
                repository.insertTask(task)
                task
            }
            updateTask(task)
        }
    }

    private suspend fun updateTask(task: Task) = repository.updateTask(task)

    fun sort() {
        val currentOrder = (_descending.value)!!
        _descending.value = !currentOrder
    }
    fun edit() {
        val currentOrder = (_editing.value)!!
        _editing.value = !currentOrder
    }

    fun completeTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    private suspend fun getTaskById(id: String): Task? = repository.getTaskById(id)

    suspend fun getCurrentTask(id: String) {
        val task = getTaskById(id) ?: return
        currentTask.value = task
    }

    fun setCurrentTask(task: Task) {

        _currentTask.value = task

    }


    fun getTask(ascending: Boolean):LiveData<List<Task>> {

        if (ascending)
            return repository.getTaskByAscendingSort()

        return repository.getTaskByDescendingSort()
    }

}