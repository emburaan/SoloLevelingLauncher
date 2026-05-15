package com.sumit.todo_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumit.todo_list.data.local.TaskEntity
import com.sumit.todo_list.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    val tasks: StateFlow<List<TaskEntity>> = repository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /**
     * Attempts to add a task. Returns false when the priority limit ([MAX_TASKS]) is reached
     * so callers can react with UI (e.g. a snackbar).
     */
    fun addTask(text: String): Boolean {
        if (tasks.value.size >= MAX_TASKS) return false
        viewModelScope.launch { repository.addTask(text) }
        return true
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch { repository.deleteTask(taskId) }
    }

    fun toggleTask(task: TaskEntity) {
        viewModelScope.launch { repository.toggleTask(task) }
    }

    companion object {
        const val MAX_TASKS = 5
    }
}
