package com.codemave.mobilecomputing.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codemave.mobilecomputing.data.entity.Category
import com.codemave.mobilecomputing.data.entity.LoginInfo
import com.codemave.mobilecomputing.data.entity.Task

/**
 * The [RoomDatabase] for this app
 */
@Database(
    entities = [Category::class, Task::class, LoginInfo::class],
    version = 19,
    exportSchema = false
)
abstract class MobileComputingDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun taskDao(): TaskDao
    abstract fun loginInfoDao(): LoginInfoDao

}