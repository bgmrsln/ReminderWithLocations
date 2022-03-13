package com.codemave.mobilecomputing.ui.edit

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.codemave.mobilecomputing.data.entity.LoginInfo
import com.codemave.mobilecomputing.ui.task.TaskViewModel

import com.google.accompanist.insets.systemBarsPadding
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Edit(
    navController: NavController,
    username: String,
    viewModel: EditViewModel = viewModel()
) {

    val coroutineScope = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize()) {
        val newUsername = rememberSaveable { mutableStateOf("") }
        val newPassword = rememberSaveable { mutableStateOf("") }
        val password = rememberSaveable { mutableStateOf("") }
        coroutineScope.launch{
            password.value = viewModel.getPasswordWithUsername(username = username)?.password ?: ""
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = newUsername.value,
                onValueChange = { data -> newUsername.value = data },
                label = { Text(username) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(

                value = newPassword.value,
                onValueChange = { data -> newPassword.value = data },

                label = { Text(password.value) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        print("hey")
                        val loginInfo: LoginInfo? = viewModel.getPasswordWithUsername(username = username)
                        val newLoginIndo= loginInfo?.copy(username= newUsername.value,
                            password= newPassword.value)
                        if (newLoginIndo != null) {
                            viewModel.updateLogin(
                                newLoginIndo
                            )
                        }
                    }
                    navController.navigate("login")
                },
                enabled = newUsername!=null && newPassword.value.length>=6,
                //if(username.value=="bgmrsln" &&  password.value== "1234") true else false,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(55.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = "Edit")
            }
            Text(text = "Your password should be longer than or equal to 6")
        }
    }
}