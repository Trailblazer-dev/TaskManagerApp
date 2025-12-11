package com.example.taskmanagerapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.taskmanagerapp.Task
import com.example.taskmanagerapp.TaskDatabaseHelper

@Composable
fun AddTaskScreen(taskId: Int? = null, onTaskSaved: () -> Unit, onCancel: () -> Unit) {
    val context = LocalContext.current
    val db = remember { TaskDatabaseHelper(context) }

    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }

    LaunchedEffect(taskId) {
        if (taskId != null) {
            val task = db.getTask(taskId)
            if (task != null) {
                title.value = task.title
                description.value = task.description
            }
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = title.value,
                onValueChange = { title.value = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description.value,
                onValueChange = { description.value = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = onCancel, modifier = Modifier.padding(end = 8.dp)) {
                    Text("Cancel")
                }
                Button(onClick = {
                    if (title.value.isNotEmpty()) {
                        val task = Task(
                            id = taskId ?: 0,
                            title = title.value,
                            description = description.value
                        )
                        if (taskId == null) {
                            db.insertTask(task)
                            Toast.makeText(context, "Task Saved", Toast.LENGTH_SHORT).show()
                        } else {
                            db.updateTask(task)
                            Toast.makeText(context, "Task Updated", Toast.LENGTH_SHORT).show()
                        }
                        onTaskSaved()
                    } else {
                        Toast.makeText(context, "Title is required", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Save")
                }
            }
        }
    }
}
