package com.example.log_in.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.log_in.AuthViewModel
import java.nio.file.WatchEvent

@Composable
fun Home_page(modifier: androidx.compose.ui.Modifier, navController: NavController, authViewModel: AuthViewModel)
{
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
    var biometricEnabled by remember { mutableStateOf(prefs.getBoolean("biometric_enabled", false)) }



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
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Unlock with biometrics")
            Spacer(Modifier.width(8.dp))
            Switch(
                checked = biometricEnabled,
                onCheckedChange = {
                    biometricEnabled = it
                    prefs.edit().putBoolean("biometric_enabled", it).apply()
                }
            )
        }

        Text("Home page", fontSize = 25.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
            authViewModel.signOut()
            }
        ) {
            Text("Sign out")
        }

    }

}