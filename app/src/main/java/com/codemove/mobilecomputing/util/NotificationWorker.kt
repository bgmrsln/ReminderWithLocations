package com.codemove.mobilecomputing.util

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.codemave.mobilecomputing.Graph.taskRepository
import kotlinx.coroutines.runBlocking
import java.util.*

class NotificationWorker(
    context: Context,
    userParameters: WorkerParameters,

) : Worker(context, userParameters) {

    override fun doWork(): Result {
        /*
        runBlocking {

            val task= taskRepository.task(inputData.getLong("id",0))

            if (task != null) {
                Log.i("NotificationWorker", "task is correct ${task.taskId}")
                if (Date().time== task.reminderTime){
                    Log.i("NotificationWorker", "SUCCESS")
                    return@runBlocking Result.success()
                }else{
                    return@runBlocking Result.failure()
                }

            }else{
                Log.i("NotificationWorker", "didnt get the task")
                return@runBlocking Result.failure()
            }



        }

         */

        return try {

            for ( i in 0..10) {
                Log.i("NotificationWorker","Counted $i")
            }
            Result.success()
        } catch(e: Exception){
            Result.failure()
        }


    }


}