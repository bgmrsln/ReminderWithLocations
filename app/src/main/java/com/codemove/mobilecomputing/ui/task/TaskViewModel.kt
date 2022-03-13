package com.codemave.mobilecomputing.ui.task

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.from
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.codemave.mobilecomputing.Graph
import com.codemave.mobilecomputing.R
import com.codemave.mobilecomputing.data.entity.Category
import com.codemave.mobilecomputing.data.entity.Task
import com.codemave.mobilecomputing.data.repository.CategoryRepository
import com.codemave.mobilecomputing.data.repository.TaskRepository
import com.codemave.mobilecomputing.ui.home.categoryTask.toDateString
import com.codemove.mobilecomputing.util.NotificationWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Math.abs
import java.util.*
import java.util.concurrent.TimeUnit
private var notificationIds=0
class TaskViewModel(
    private val taskRepository: TaskRepository = Graph.taskRepository,
    private val categoryRepository: CategoryRepository = Graph.categoryRepository,
    private val context: Context= Graph.appContext
): ViewModel() {

    private val _state = MutableStateFlow(TaskViewState())

    val state: StateFlow<TaskViewState>
        get() = _state

    suspend fun saveTask(task: Task): Long {
        if(task.notificationWanted){
            newReminderNotification(task)
            setOneTimeNotification(task)
        }
        if(task.earlyNotification){
            setOneTimeEarlyNotification(task)
        }

        return taskRepository.addTask(task)
    }

    init {
        createNotificationChannel(context= Graph.appContext   )

        viewModelScope.launch {
            categoryRepository.categories().collect { categories ->
                _state.value = TaskViewState(categories)
            }
        }
    }
}
private fun reminderTimeNotification(task: Task){
    val notificationId = notificationIds
    notificationIds +=1

    val builder = NotificationCompat.Builder(Graph.appContext, "CHANNEL_ID")
        .setSmallIcon(R.drawable.reminders)
        .setContentTitle("You have a reminder")
        .setContentText(task.taskTitle)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setColor(0xA61C0D)
    with(NotificationManagerCompat.from(Graph.appContext)) {
        notify(notificationId, builder.build())
    }

}
private fun earlyReminderTimeNotification(task: Task){
    val notificationId = notificationIds
    notificationIds +=1
    val builder = NotificationCompat.Builder(Graph.appContext, "CHANNEL_ID")
        .setSmallIcon(R.drawable.early)
        .setContentTitle("You have a task due in 5 minutes")
        .setContentText(task.taskTitle)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setColor(0xEDED21)
    with(NotificationManagerCompat.from(Graph.appContext)) {
        notify(notificationId, builder.build())
    }

}

private fun setOneTimeNotification(task: Task){
    val workManager= WorkManager.getInstance(Graph.appContext)
    val constraints= androidx.work.Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
    val data= Data.Builder()
    data.putLong("id", task.taskId)
    //if there is a reminder time : if we enter the task area and reminder time has come there is a notif
    if(task.reminderTime!= null){
        if(task.taskLocationX!=null&& task.currentLocationX!=null &&
            abs(task.currentLocationX-task.taskLocationX)<0.08 &&
            task.taskLocationY!= null && task.currentLocationY!= null&&
            abs(task.taskLocationY- task.currentLocationY)<0.08){

            val notificationWorker= OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(task.reminderTime-Date().time, TimeUnit.MILLISECONDS)
                .setConstraints(constraints)
                .setInputData(data.build())
                .build()

            workManager.enqueue(notificationWorker)

            //Monitoring for state of work
            workManager.getWorkInfoByIdLiveData(notificationWorker.id)
                .observeForever { workInfo ->
                    if(workInfo.state == WorkInfo.State.SUCCEEDED) {
                        reminderTimeNotification(task)
                    }
                    else{

                        //createSuccessNotification(task)
                    }
                }

        }else{
            val notificationWorker= OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(task.reminderTime-Date().time, TimeUnit.MILLISECONDS)
                .setConstraints(constraints)
                .setInputData(data.build())
                .build()

            workManager.enqueue(notificationWorker)

            //Monitoring for state of work
            workManager.getWorkInfoByIdLiveData(notificationWorker.id)
                .observeForever { workInfo ->
                    if(workInfo.state == WorkInfo.State.SUCCEEDED) {
                        reminderTimeNotification(task)
                    }
                    else{

                        //createSuccessNotification(task)
                    }
                }
        }
    }else{
        if(task.taskLocationX!= -1.0 &&
            abs(task.currentLocationX-task.taskLocationX)<0.08 &&
            task.taskLocationY!= -1.0&&
            abs(task.taskLocationY- task.currentLocationY)<0.08){

            val notificationWorker= OneTimeWorkRequestBuilder<NotificationWorker>()
                .setConstraints(constraints)
                .setInputData(data.build())
                .build()

            workManager.enqueue(notificationWorker)

            //Monitoring for state of work
            workManager.getWorkInfoByIdLiveData(notificationWorker.id)
                .observeForever { workInfo ->
                    if(workInfo.state == WorkInfo.State.SUCCEEDED) {
                        reminderTimeNotification(task)
                    }
                    else{

                        //createSuccessNotification(task)
                    }
                }

        }

    }


}
private fun setOneTimeEarlyNotification(task: Task){
    val workManager= WorkManager.getInstance(Graph.appContext)
    val constraints= androidx.work.Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
    val data= Data.Builder()
    data.putLong("id", task.taskId)


    if(task.reminderTime!= null){
        val notificationWorker= OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(task.reminderTime-Date().time- 300000, TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        workManager.enqueue(notificationWorker)

        //Monitoring for state of work
        workManager.getWorkInfoByIdLiveData(notificationWorker.id)
            .observeForever { workInfo ->
                if(workInfo.state == WorkInfo.State.SUCCEEDED) {
                    earlyReminderTimeNotification(task)
                }
                else{


                }
            }
    }

}

private fun newReminderNotification(task: Task){
    val notificationId = notificationIds
    notificationIds +=1
    val builder= NotificationCompat.Builder(Graph.appContext, "CHANNEL_ID")
        .setSmallIcon(R.drawable.add_reminder)
        .setContentTitle("New reminder")
        .setContentText("Job is due to: ${task.reminderTime?.toDateString()}\n ${task.taskTitle}")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setStyle(NotificationCompat.BigTextStyle().bigText("Job is due to: ${task.reminderTime?.toDateString()}\n ${task.taskTitle}"))
        .setColor(0xFF1C5984.toInt())
    with(from(Graph.appContext)){
        //notification id is unique for each notification that you define
        notify(notificationId, builder.build())

    }

}

private fun createNotificationChannel(context: Context){
    //the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val name= "NotificationChannelName"
        val descriptionText= "NotificationChannelDescriptionText"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel= NotificationChannel("CHANNEL_ID", name, importance).apply{
            description= descriptionText
        }
        //register the channel with the system
        val notificationManager: NotificationManager= context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)



    }


}



data class TaskViewState(
    val categories: List<Category> = emptyList()
)