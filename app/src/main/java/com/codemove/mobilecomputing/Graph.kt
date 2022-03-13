package com.codemave.mobilecomputing

import android.content.Context
import androidx.room.Room
import com.codemave.mobilecomputing.data.repository.CategoryRepository
import com.codemave.mobilecomputing.data.repository.LoginInfoRepository
import com.codemave.mobilecomputing.data.repository.TaskRepository
import com.codemave.mobilecomputing.data.room.MobileComputingDatabase

/**
 * A simple singleton dependency graph
 *
 * For a real app, please use something like Koin/Dagger/Hilt instead
 */
object Graph {
    lateinit var database: MobileComputingDatabase
    lateinit var appContext: Context
    val categoryRepository by lazy {
        CategoryRepository(
            categoryDao = database.categoryDao()
        )
    }

    val taskRepository by lazy {
        TaskRepository(
            taskDao = database.taskDao()
        )
    }
    val loginInfoRepository by lazy {
        LoginInfoRepository(
            loginInfoDao = database.loginInfoDao()
        )
    }


    fun provide(context: Context) {
        appContext= context
        database = Room.databaseBuilder(context, MobileComputingDatabase::class.java, "mcData.db")
            .fallbackToDestructiveMigration() // don't use this in production app
            .build()
    }
}