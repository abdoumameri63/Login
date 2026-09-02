package com.example.log_in.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.log_in.AuthViewModel

@Composable
fun Signup_page (modifier: Modifier, navController: NavController, authViewModel: AuthViewModel)
{
    var email by remember{mutableStateOf("")}

    var password by remember{mutableStateOf("")}

    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current


    LaunchedEffect(authState) {

        when (authState) {

            is AuthViewModel.AuthState.Authenticated -> {
                navController.navigate("home") {
                    popUpTo("Signup") {
                        inclusive = true
                    }
                }
            }

            is AuthViewModel.AuthState.Error -> {
                val message =
                    (authState as AuthViewModel.AuthState.Error).message

                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> Unit
        }
    }


    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(text="Signup page", fontSize = 30.sp)
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = email,
            onValueChange ={email=it},
            label ={
                Text(text = "Email")
            }
        )

        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = password,
            onValueChange ={password=it},
            label ={
                Text(text = "Password")
            },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier= Modifier.height(20.dp))

        Button(onClick = {
            authViewModel.signUp(email, password)

        }) {
            Text(text = "Create acount")
        }

        Spacer(modifier = Modifier.height(15.dp))
        TextButton(
            onClick = {
                navController.navigate("Login")
            },
        ) {
            Text(text = "I already have a compte", fontSize = 20.sp)
        }


    }

}