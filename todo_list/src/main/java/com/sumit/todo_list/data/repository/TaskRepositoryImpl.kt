package com.sumit.todo_list.data.repository

import com.sumit.todo_list.data.local.TaskDao
import com.sumit.todo_list.data.local.TaskEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val dao: TaskDao
) : TaskRepository {

    override fun observeAll(): Flow<List<TaskEntity>> = dao.observeAll()

    override suspend fun getAll(): List<TaskEntity> = dao.getAll()

    override suspend fun addTask(text: String) {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return
        dao.insert(TaskEntity(text = trimmed))
    }

    override suspend fun deleteTask(taskId: Int) = dao.deleteById(taskId)

    override suspend fun toggleTask(task: TaskEntity) =
        dao.update(task.copy(isChecked = !task.isChecked))
}
