package com.example.log_in.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.log_in.AuthViewModel
import com.example.log_in.BiometricHelper

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController, authViewModel) }
        composable("Login") { Login_page(modifier, navController, authViewModel) }
        composable("Signup") { Signup_page(modifier, navController, authViewModel) }
        composable("Home") { Home_page(modifier, navController, authViewModel) }
        composable("VerifyEmail") { VerifyEmail_page(navController, authViewModel) }
        composable("ForgotPassword") { ForgotPassword_page (navController, authViewModel) }
    }
}

@Composable
fun SplashScreen(navController: androidx.navigation.NavController, authViewModel: AuthViewModel) {
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current
    val activity = context as? androidx.fragment.app.FragmentActivity
    var biometricChecked by remember { mutableStateOf(false) }

    val prefs = context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
    val biometricEnabled = prefs.getBoolean("biometric_enabled", false)

    LaunchedEffect(authState) {
        when (authState) {
            is AuthViewModel.AuthState.Authenticated -> {
                if (biometricEnabled && activity != null && BiometricHelper.isAvailable(activity) && !biometricChecked) {
                    BiometricHelper.authenticate(
                        activity,
                        onSuccess = {
                            biometricChecked = true
                            navController.navigate("Home") { popUpTo("splash") { inclusive = true } }
                        },
                        onError = { errorMsg ->
                            // "Use password instead" or biometric hardware error -> fall back to login
                            navController.navigate("Login") { popUpTo("splash") { inclusive = true } }
                        }
                    )
                } else {
                    navController.navigate("Home") { popUpTo("splash") { inclusive = true } }
                }
            }

            is AuthViewModel.AuthState.UnAuthenticated ->
                navController.navigate("Login") { popUpTo("splash") { inclusive = true } }

            is AuthViewModel.AuthState.EmailNotVerified ->
                navController.navigate("VerifyEmail") { popUpTo("splash") { inclusive = true } }

            else -> Unit
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}