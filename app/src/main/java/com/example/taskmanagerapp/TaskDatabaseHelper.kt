package com.example.taskmanagerapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "tasks.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_TASKS = "tasks"
        private const val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_DESCRIPTION = "description"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE " + TABLE_TASKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT" + ")")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        onCreate(db)
    }

    fun insertTask(task: Task): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(KEY_TITLE, task.title)
            put(KEY_DESCRIPTION, task.description)
        }
        val id = db.insert(TABLE_TASKS, null, contentValues)
        db.close()
        return id
    }

    fun getAllTasks(): List<Task> {
        val taskList = mutableListOf<Task>()
        val selectQuery = "SELECT * FROM $TABLE_TASKS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val task = Task(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                    title = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION))
                )
                taskList.add(task)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return taskList
    }

    fun getTask(id: Int): Task? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_TASKS,
            arrayOf(KEY_ID, KEY_TITLE, KEY_DESCRIPTION),
            "$KEY_ID=?",
            arrayOf(id.toString()),
            null,
            null,
            null,
            null
        )
        var task: Task? = null
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                task = Task(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                    title = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION))
                )
            }
            cursor.close()
        }
        db.close()
        return task
    }
    
    fun updateTask(task: Task): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(KEY_TITLE, task.title)
            put(KEY_DESCRIPTION, task.description)
        }
        val success = db.update(TABLE_TASKS, contentValues, "$KEY_ID=?", arrayOf(task.id.toString()))
        db.close()
        return success
    }

    fun deleteTask(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_TASKS, "$KEY_ID=?", arrayOf(id.toString()))
        db.close()
    }
}
