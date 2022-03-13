package com.codemave.mobilecomputing.ui.editTask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codemave.mobilecomputing.Graph
import com.codemave.mobilecomputing.data.entity.Category
import com.codemave.mobilecomputing.data.entity.Task
import com.codemave.mobilecomputing.data.repository.CategoryRepository
import com.codemave.mobilecomputing.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditTaskViewModel(
    private val taskRepository: TaskRepository = Graph.taskRepository,
    private val categoryRepository: CategoryRepository = Graph.categoryRepository
): ViewModel() {
    private val _state = MutableStateFlow(EditTaskViewState())

    val state: StateFlow<EditTaskViewState>
        get() = _state

    //returns the id of the task??
    suspend fun updateTask(task: Task): Int {

        return taskRepository.updateTask(task)
    }
    suspend fun getTaskwithTaskId(taskId: Long): Task?{
        return taskRepository.task(taskId)
    }

    init {
        viewModelScope.launch {
            categoryRepository.categories().collect { categories ->
                _state.value = EditTaskViewState(categories)
            }
        }
    }
}

data class EditTaskViewState(
    val categories: List<Category> = emptyList()
)