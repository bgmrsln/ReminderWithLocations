 package com.codemave.mobilecomputing.ui.home.categoryTask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codemave.mobilecomputing.Graph
import com.codemave.mobilecomputing.Graph.loginInfoRepository
import com.codemave.mobilecomputing.data.entity.Task
import com.codemave.mobilecomputing.data.repository.TaskRepository
import com.codemave.mobilecomputing.data.room.TaskToCategory
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.lang.Math.abs
import java.util.*


class CategoryTaskViewModel(
    private val categoryId: Long,
    private val taskRepository: TaskRepository = Graph.taskRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CategoryTaskViewState())

    val state: StateFlow<CategoryTaskViewState>
        get() = _state

    init {
        viewModelScope.launch {

            taskRepository.tasksInCategory(categoryId).collect { list ->

                val filteredList2= list.filter {
                    if (it.task.reminderTime!! <= Date().time){
                        taskRepository.updateTask(it.task.copy(bool= true)
                        )
                    }
                    it.task.bool &&

                    abs(it.task.currentLocationY!! - it.task.taskLocationY!!)<0.2 &&
                            abs(it.task.currentLocationX!! - it.task.taskLocationX!!
                    )<0.2


                }
                _state.value = CategoryTaskViewState(
                    tasks = filteredList2
                )
            }

        }
    }
    suspend fun deleteTask(task: Task): Int {
        return taskRepository.deleteTask(task)
    }
}

data class CategoryTaskViewState(
    val tasks: List<TaskToCategory> = emptyList()
)