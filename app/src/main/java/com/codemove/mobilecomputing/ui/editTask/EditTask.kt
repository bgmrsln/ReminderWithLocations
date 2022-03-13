package com.codemave.mobilecomputing.ui.editTask

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codemave.mobilecomputing.data.entity.Category
import com.codemave.mobilecomputing.data.entity.Task

import com.google.accompanist.insets.systemBarsPadding
import java.util.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun EditTask(
    //I can send the task as a parameter to here
    onBackPress: () -> Unit,
    taskId: String,
    viewModel: EditTaskViewModel = viewModel()
) {
    val viewState by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val oldMessage = rememberSaveable { mutableStateOf("") }
    val title = rememberSaveable { mutableStateOf("") }
    val category = rememberSaveable { mutableStateOf("") }
    val reminderTime = rememberSaveable { mutableStateOf("") }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            TopAppBar {
                IconButton(
                    onClick = onBackPress
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
                Text(text = "Task")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(16.dp)
            ) {
                coroutineScope.launch {
                    //task id should come when it is clicked
                    val task= viewModel.getTaskwithTaskId(taskId.toLong())
                    if (task != null) {
                        oldMessage.value= task.taskTitle
                    }

                }
                OutlinedTextField(

                    value = title.value,
                    onValueChange = { title.value = it },

                    label = { Text(text = oldMessage.value)},
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                CategoryListDropdown(
                    viewState = viewState,
                    category = category
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = reminderTime.value,
                    onValueChange = { reminderTime.value = it },
                    label = { Text(text = "Due Date as yyyy/MM/dd")},
                    modifier = Modifier.fillMaxWidth(),

                )
                Spacer(modifier = Modifier.height(10.dp))
                val sdf=  SimpleDateFormat("yyyy/MM/dd")

                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    enabled = true,
                    onClick = {

                        coroutineScope.launch {
                            //task id should come when it is clicked
                            val task= viewModel.getTaskwithTaskId(taskId.toLong())
                            val newTask=
                                task?.copy(taskTitle = title.value,
                                    reminderTime = sdf.parse(reminderTime.value).getTime(),
                                    creationTime = Date().time,
                                    taskCategoryId = getCategoryId(viewState.categories, category.value),
                                    creatorId = "b",
                                )
                            if (newTask != null) {
                                viewModel.updateTask(newTask)
                            }
                        }


                        onBackPress()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(55.dp)
                ) {
                    Text("Save edited task")
                }
            }
        }
    }
}

private fun getCategoryId(categories: List<Category>, categoryName: String): Long {
    return categories.first { category -> category.name == categoryName }.id
}

@Composable
private fun CategoryListDropdown(
    viewState: EditTaskViewState,
    category: MutableState<String>
) {
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded) {
        Icons.Filled.ArrowDropUp // requires androidx.compose.material:material-icons-extended dependency
    } else {
        Icons.Filled.ArrowDropDown
    }

    Column {
        OutlinedTextField(
            value = category.value,
            onValueChange = { category.value = it},
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Category") },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            viewState.categories.forEach { dropDownOption ->
                DropdownMenuItem(
                    onClick = {
                        category.value = dropDownOption.name
                        expanded = false
                    }
                ) {
                    Text(text = dropDownOption.name)
                }

            }
        }
    }
}