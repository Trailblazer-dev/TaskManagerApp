package com.example.taskmanagerapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanagerapp.Task
import com.example.taskmanagerapp.ui.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    taskId: Int? = null,
    onTaskSaved: () -> Unit,
    onCancel: () -> Unit,
    taskViewModel: TaskViewModel = viewModel(factory = TaskViewModel.Factory)
) {
    val context = LocalContext.current
    val taskToEdit by taskViewModel.selectedTask.collectAsState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    LaunchedEffect(key1 = taskId) {
        if (taskId != null) {
            taskViewModel.loadTask(taskId)
        } else {
            taskViewModel.clearSelectedTask()
        }
    }

    LaunchedEffect(key1 = taskToEdit) {
        taskToEdit?.let {
            title = it.title
            description = it.description
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (taskId == null) "Add Task" else "Edit Task") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onCancel) {
                    Text("Cancel")
                }
                Button(onClick = {
                    if (title.isNotEmpty()) {
                        val task = Task(
                            id = taskId ?: 0,
                            title = title,
                            description = description
                        )
                        if (taskId == null) {
                            taskViewModel.addTask(task)
                            Toast.makeText(context, "Task Saved", Toast.LENGTH_SHORT).show()
                        } else {
                            taskViewModel.updateTask(task)
                            Toast.makeText(context, "Task Updated", Toast.LENGTH_SHORT).show()
                        }
                        onTaskSaved()
                    } else {
                        Toast.makeText(context, "Title is required", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text(if (taskId == null) "Save" else "Update")
                }
            }
        }
    }
}
