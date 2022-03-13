package com.codemave.mobilecomputing.ui.task

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.codemave.mobilecomputing.data.entity.Category
import com.google.accompanist.insets.systemBarsPadding
import com.google.android.gms.maps.model.LatLng
import java.util.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Task(
    onBackPress: () -> Unit,
    viewModel: TaskViewModel = viewModel(),
    navController: NavController
) {
    val viewState by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val title = rememberSaveable { mutableStateOf("") }
    val category = rememberSaveable { mutableStateOf("") }
    val reminderTime = rememberSaveable { mutableStateOf("") }

    val latlng = navController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<LatLng>("location_data")
        ?.value
    val currentLatlng = navController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<LatLng>("virtual_location_data")
        ?.value
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
                OutlinedTextField(
                    value = title.value,
                    onValueChange = { title.value = it },
                    label = { Text(text = "Task message")},
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
                    label = { Text(text = "Due Date as yyyy/MM/dd hh:mm")},
                    modifier = Modifier.fillMaxWidth(),

                )
                Spacer(modifier = Modifier.height(40.dp))
                Row(modifier = Modifier

                    .systemBarsPadding()){
                    if (latlng==null){
                        OutlinedButton(
                            onClick = { navController.navigate("map")},
                            modifier= Modifier.height(55.dp)
                        ){
                            Text(text= "Task location")
                        }
                    }else{
                        Text(
                            text= "Lat: ${latlng.latitude}, \nLng: ${latlng.longitude}"
                        )
                    }
                    Spacer(modifier= Modifier.width(10.dp))
                    if (currentLatlng==null){
                        OutlinedButton(
                            onClick = { navController.navigate("secondmap")},
                            modifier= Modifier.height(55.dp)
                        ){
                            Text(text= "Current location")
                        }
                    }else{
                        Text(
                            text= "Lat: ${currentLatlng.latitude}, \nLng: ${currentLatlng.longitude}"
                        )
                    }



                }


                Spacer(modifier = Modifier.height(40.dp))
                val isChecked= remember{ mutableStateOf(false)}
                Row(modifier = Modifier

                    .systemBarsPadding()){


                    Text(text = "Do you want notifications?")
                    Spacer(modifier = Modifier.width(10.dp))
                    Checkbox(checked = isChecked.value, onCheckedChange = {isChecked.value= it} )
                }




                Spacer(modifier = Modifier.height(40.dp))

                val isMultipleChecked= remember{ mutableStateOf(false)}
                Row(modifier = Modifier

                    .systemBarsPadding()){


                    Text(text = "Do you want a 5-minute earlier reminder?")
                    Spacer(modifier = Modifier.width(10.dp))
                    Checkbox(checked = isMultipleChecked.value, onCheckedChange = {isMultipleChecked.value= it} )
                }




                Spacer(modifier = Modifier.height(40.dp))
                val sdf=  SimpleDateFormat("yyyy/MM/dd hh:mm")



                Button(
                    enabled = true,
                    onClick = {
                        coroutineScope.launch {
                            viewModel.saveTask(
                                com.codemave.mobilecomputing.data.entity.Task(
                                    taskTitle = title.value,
                                    //update time


                                    reminderTime = if(reminderTime.value=="") 0 else sdf.parse(reminderTime.value).getTime(),
                                    creationTime = Date().time ,
                                    //System.currentTimeMillis(),
                                    taskCategoryId = getCategoryId(viewState.categories, category.value),
                                    taskLocationX = if(latlng!= null) latlng.latitude else -1.0,
                                    taskLocationY = if(latlng!= null) latlng.longitude else -1.0,
                                    creatorId = "b",
                                    reminderSeen = null,
                                    bool = if(reminderTime.value!= "" && sdf.parse(reminderTime.value).getTime() <= Date().time) true else false,
                                    notificationWanted = isChecked.value,
                                    earlyNotification = isMultipleChecked.value,

                                    currentLocationX = if(currentLatlng!= null) currentLatlng.latitude else 65.03,
                                    currentLocationY = if (currentLatlng!= null) currentLatlng.longitude else 25.27



                                )
                            )
                        }
                        onBackPress()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(55.dp)
                ) {
                    Text("Save task")
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
    viewState: TaskViewState,
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