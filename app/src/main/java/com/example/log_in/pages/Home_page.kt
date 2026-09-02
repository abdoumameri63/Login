package com.example.log_in.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.log_in.AuthViewModel

@Composable
fun Home_page(modifier: androidx.compose.ui.Modifier, navController: NavController, authViewModel: AuthViewModel)
{
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState)
    {
        when(authState)
        {
            is AuthViewModel.AuthState.UnAuthenticated -> navController.navigate("Login")
            else -> Unit
        }
    }
    Column (modifier = Modifier.fillMaxSize(),Arrangement.Center,Alignment.CenterHorizontally)
    {
        Text("Home page", fontSize = 25.sp)
        TextButton(
            onClick = {
            authViewModel.signOut()
            }
        ) {
            Text("Sign out")
        }

    }

}