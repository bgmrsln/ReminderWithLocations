package com.codemave.mobilecomputing.ui.login


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.codemave.mobilecomputing.R
import com.codemave.mobilecomputing.data.entity.LoginInfo
import com.codemave.mobilecomputing.ui.profile.ProfileViewModel
import com.google.accompanist.insets.systemBarsPadding
import kotlinx.coroutines.launch

@Composable
fun Login(
    navController: NavController,
    viewModel: LoginViewModel = viewModel(),
    viewModel2: ProfileViewModel= viewModel()


) {

    val coroutineScope = rememberCoroutineScope()
    val onLoginInfoSelected= viewModel2::onLoginSelected

    Surface(modifier = Modifier.fillMaxSize()) {
        val username = rememberSaveable { mutableStateOf("") }
        val password = rememberSaveable { mutableStateOf("") }
        Image(painter = painterResource(id = R.drawable.pink),
            contentDescription =null,
            modifier = Modifier,
            alignment = Alignment.Center,
            contentScale = ContentScale.FillBounds
        )

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
                value = username.value,
                onValueChange = { data -> username.value = data },
                label = { Text("Username")},
                modifier = Modifier.fillMaxWidth(),

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                colors= TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor=White,

                )


            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = password.value,
                onValueChange = { data -> password.value = data },
                label = { Text("Password")},
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    var loginInfo= LoginInfo(username="bgmrsln", password="123456", currentLocationY = null, currentLocationX = null)
                    coroutineScope.launch {
                        loginInfo=  com.codemave.mobilecomputing.data.entity.LoginInfo(
                            username = username.value,
                            password = password.value,
                            currentLocationY = null,
                            currentLocationX = null

                            )
                        if(
                            viewModel.checkUsername(
                                loginInfo
                            )
                        ){
                            println(username.value)
                            navController.navigate("home/${username.value}")
                            //
                        }

                    }
                    onLoginInfoSelected(loginInfo)


                },



                enabled = password.value.length >= 6,
                //if(username.value=="bgmrsln" &&  password.value== "1234") true else false,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(55.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {  navController.navigate("signup") },
                enabled = true,
                //if(username.value=="bgmrsln" &&  password.value== "1234") true else false,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(55.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = "SignUp")
            }

        }
    }
}

