package com.example.log_in.pages

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.log_in.AuthViewModel

@Composable
fun MyAppNavigation(modifier : Modifier = Modifier,authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Login", builder = {
        composable ("Login"){
            Login_page(modifier ,navController,authViewModel)
        }
        composable ("Signup"){
            Signup_page(modifier ,navController,authViewModel)
        }
        composable ("Home"){
            Home_page(modifier ,navController,authViewModel)
        }
    })
}













































