package com.codemave.mobilecomputing.data.room

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.codemave.mobilecomputing.data.entity.Category
import com.codemave.mobilecomputing.data.entity.Task
import java.util.*

class TaskToCategory {
    @Embedded
    lateinit var task: Task

    @Relation(parentColumn = "task_category_id", entityColumn = "id")
    lateinit var _categories: List<Category>

    @get:Ignore
    val category: Category
        get() = _categories[0]

    /**
     * Allow this class to be destructured by consumers
     */
    operator fun component1() = task
    operator fun component2() = category

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is TaskToCategory -> task == other.task && _categories == other._categories
        else -> false
    }

    override fun hashCode(): Int = Objects.hash(task, _categories)
}