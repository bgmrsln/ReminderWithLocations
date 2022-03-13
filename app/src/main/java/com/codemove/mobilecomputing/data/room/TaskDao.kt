package com.codemave.mobilecomputing.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.codemave.mobilecomputing.data.entity.LoginInfo
import com.codemave.mobilecomputing.data.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TaskDao {
    @Query("""
        SELECT tasks.* FROM tasks
        INNER JOIN categories ON tasks.task_category_id = categories.id
        WHERE task_category_id = :categoryId
    """)
    abstract fun tasksFromCategory(categoryId: Long): Flow<List<TaskToCategory>>

    @Query(value = "SELECT * FROM tasks WHERE id = :taskId")
    abstract suspend fun getTaskWithTaskId(taskId: Long): Task?

    @Query("""SELECT * FROM tasks WHERE id = :taskId""")
    abstract fun task(taskId: Long): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: Task): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(entity: Task): Int

    @Delete
    abstract suspend fun delete(entity: Task): Int
}