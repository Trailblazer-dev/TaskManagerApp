package com.example.taskmanagerapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.taskmanagerapp.Task
import com.example.taskmanagerapp.TaskDatabaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val db = TaskDatabaseHelper(application)

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> = _selectedTask
    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            _tasks.value = db.getAllTasks()
        }
    }

    fun loadTask(id: Int) {
        viewModelScope.launch {
            _selectedTask.value = db.getTask(id)
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            db.insertTask(task)
            loadTasks()
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            db.updateTask(task)
            loadTasks()
        }
    }

    fun deleteTask(id: Int) {
        viewModelScope.launch {
            db.deleteTask(id)
            loadTasks()
        }
    }

    fun clearSelectedTask() {
        _selectedTask.value = null
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                TaskViewModel(application)
            }
        }
    }
}
