package com.codemave.mobilecomputing.ui.home.categoryTask

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.codemave.mobilecomputing.R
import com.codemave.mobilecomputing.data.entity.Category
import com.codemave.mobilecomputing.data.entity.Task
import com.codemave.mobilecomputing.data.room.TaskToCategory

import com.codemave.mobilecomputing.util.viewModelProviderFactoryOf
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CategoryTask(
    categoryId: Long,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val viewModel: CategoryTaskViewModel = viewModel(
        key = "category_list_$categoryId",

        factory = viewModelProviderFactoryOf { CategoryTaskViewModel(categoryId) }
    )
    val viewState by viewModel.state.collectAsState()

    Column(modifier = modifier) {
        TaskList(
            list = viewState.tasks,
            viewModel = viewModel,
            navController = navController
        )
    }
}


@Composable
private fun TaskList(
    list: List<TaskToCategory>,
    viewModel: CategoryTaskViewModel,
    navController: NavController

) {
    LazyColumn(
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.Center
    ) {

        items(list) { item ->
            TaskListItem(
                task = item.task,
                category = item.category,
                //put edit here
                onClick = {navController.navigate("editTask/${item.task.taskId}")},
                modifier = Modifier.fillParentMaxWidth(),
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun TaskListItem(
    task: Task,
    category: Category,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CategoryTaskViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    //val coroutineScope = rememberCoroutineScope()
    ConstraintLayout(modifier = modifier.clickable { onClick() }) {
        val (divider, taskTitle, taskCategory, icon, date) = createRefs()
        Divider(
            Modifier.constrainAs(divider) {
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)
                width = Dimension.fillToConstraints
            }
        )

        // title
        Text(
            text = task.taskTitle,
            maxLines = 1,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.constrainAs(taskTitle) {
                linkTo(
                    start = parent.start,
                    end = icon.start,
                    startMargin = 24.dp,
                    endMargin = 16.dp,
                    bias = 0f // float this towards the start. this was is the fix we needed
                )
                top.linkTo(parent.top, margin = 10.dp)
                width = Dimension.preferredWrapContent
            }
        )

        // category
        Text(
            text = category.name,
            maxLines = 1,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.constrainAs(taskCategory) {
                linkTo(
                    start = parent.start,
                    end = icon.start,
                    startMargin = 24.dp,
                    endMargin = 8.dp,
                    bias = 0f // float this towards the start. this was is the fix we needed
                )
                top.linkTo(taskTitle.bottom, margin = 6.dp)
                bottom.linkTo(parent.bottom, 10.dp)
                width = Dimension.preferredWrapContent
            }
        )

        // date
        task.reminderTime?.let {
            Text(
                text = it.toDateString(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.constrainAs(date) {
                linkTo(
                    start = taskCategory.end,
                    end = icon.start,
                    startMargin = 8.dp,
                    endMargin = 16.dp,
                    bias = 0f // float this towards the start. this was is the fix we needed
                )
                centerVerticallyTo(taskCategory)
                top.linkTo(taskTitle.bottom, 6.dp)
                bottom.linkTo(parent.bottom, 10.dp)
            }
            )
        }

        // icon
        IconButton(
            onClick = {
                coroutineScope.launch {
                    //task id should come when it is clicked
                    viewModel.deleteTask(task)
                }
                      },
            modifier = Modifier
                .size(50.dp)
                .padding(6.dp)
                .constrainAs(icon) {
                    top.linkTo(parent.top, 10.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                    end.linkTo(parent.end)
                }
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = stringResource(R.string.check_mark)
            )
        }
    }
}



public fun Long.toDateString(): String {
    return SimpleDateFormat("MMMM dd, yyyy hh:mm", Locale.getDefault()).format(Date(this))

}