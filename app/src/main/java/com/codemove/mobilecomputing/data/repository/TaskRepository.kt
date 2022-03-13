package com.codemave.mobilecomputing.data.repository

import com.codemave.mobilecomputing.data.entity.LoginInfo
import com.codemave.mobilecomputing.data.entity.Task
import com.codemave.mobilecomputing.data.room.TaskDao
import com.codemave.mobilecomputing.data.room.TaskToCategory
import kotlinx.coroutines.flow.Flow

/**
 * A data repository for [Task] instances
 */
class TaskRepository(
    private val taskDao: TaskDao
) {
    /**
     * Returns a flow containing the list of tasks associated with the category with the
     * given [categoryId]
     */
    fun tasksInCategory(categoryId: Long) : Flow<List<TaskToCategory>> {
        return taskDao.tasksFromCategory(categoryId)
    }

    /**
     * Add a new [Task] to the task store
     */
    suspend fun addTask(task: Task) = taskDao.insert(task)
    suspend fun updateTask(task: Task)= taskDao.update(task)
    //gets a task with task Id
    suspend fun task(taskId: Long): Task? = taskDao.getTaskWithTaskId(taskId)

    suspend fun deleteTask(task:Task)= taskDao.delete(task)


}