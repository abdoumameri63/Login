package com.example.log_in.pages

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.log_in.AuthViewModel

@Composable
fun Login_page (modifier: Modifier= Modifier, navController: NavController, authViewModel: AuthViewModel)
{
    var email by remember{mutableStateOf("")}
    var password by remember{mutableStateOf("")}


    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(text="Log in page", fontSize = 30.sp)
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

        }) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(15.dp))
        TextButton(
            onClick = {
                navController.navigate("Signup")
            },
        ) {
            Text(text = "create a compte?", fontSize = 20.sp)
        }


    }

}