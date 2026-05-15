package com.sumit.todo_list.data.repository

import com.sumit.todo_list.data.local.TaskEntity
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun observeAll(): Flow<List<TaskEntity>>
    suspend fun getAll(): List<TaskEntity>
    suspend fun addTask(text: String)
    suspend fun deleteTask(taskId: Int)
    suspend fun toggleTask(task: TaskEntity)
}
