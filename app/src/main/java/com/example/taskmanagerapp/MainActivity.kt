package com.example.taskmanagerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.ui.platform.LocalContext
import com.example.taskmanagerapp.ui.screens.AddTaskScreen
import com.example.taskmanagerapp.ui.screens.SettingsScreen
import com.example.taskmanagerapp.ui.screens.TaskListScreen
import com.example.taskmanagerapp.ui.theme.TaskManagerAppTheme
import com.example.taskmanagerapp.util.SettingsManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settingsManager = SettingsManager(this)
        setContent {
            TaskManagerAppTheme(darkTheme = settingsManager.isDarkTheme.value) {
                AppNavigator(settingsManager)
            }
        }
    }
}

@Composable
fun AppNavigator(settingsManager: SettingsManager) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "taskList") {
        composable("taskList") {
            TaskListScreen(
                onAddTask = { navController.navigate("addTask") },
                onEditTask = { taskId -> navController.navigate("editTask/$taskId") },
                onGoToSettings = { navController.navigate("settings") }
            )
        }
        composable("addTask") {
            AddTaskScreen(
                onTaskSaved = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }
        composable(
            "editTask/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId")
            require(taskId != null)
            AddTaskScreen(
                taskId = taskId,
                onTaskSaved = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }
        composable("settings") {
            SettingsScreen(
                settingsManager = settingsManager,
                onNavigateUp = { navController.popBackStack() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TaskManagerAppTheme {
        AppNavigator(settingsManager = SettingsManager(LocalContext.current))
    }
}
