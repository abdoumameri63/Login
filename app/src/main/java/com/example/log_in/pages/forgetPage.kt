package com.example.log_in.pages
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.log_in.AuthViewModel


    @Composable
    fun ForgotPassword_page(navController: NavController, authViewModel: AuthViewModel) {
        var email by remember { mutableStateOf("") }
        val authState by authViewModel.authState.collectAsState()
        val context = LocalContext.current
        val isLoading = authState is AuthViewModel.AuthState.Loading
        val resetSent = authState is AuthViewModel.AuthState.PasswordResetSent

        LaunchedEffect(authState) {
            if (authState is AuthViewModel.AuthState.Error) {
                val message = (authState as AuthViewModel.AuthState.Error).message
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors = listOf(Color(0xFF6A5AE0), Color(0xFFF5F5F9)))),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Reset Password", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(
                    "We'll email you a link to reset it",
                    fontSize = 15.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )

                Spacer(modifier = Modifier.height(28.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (resetSent) {
                            Text(
                                text = "✅ Reset link sent! Check your inbox (and spam folder) for instructions.",
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                color = Color(0xFF2E7D32)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    authViewModel.resetToUnauthenticated()
                                    navController.navigate("Login") { popUpTo("Login") { inclusive = true } }
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth().height(50.dp)
                            ) {
                                Text("Back to Login", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                            }
                        } else {
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email") },
                                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = { authViewModel.sendPasswordReset(email) },
                                enabled = !isLoading,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth().height(50.dp)
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
                                } else {
                                    Text("Send Reset Link", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            TextButton(onClick = {
                                authViewModel.resetToUnauthenticated()
                                navController.navigate("Login")
                            }) {
                                Text("Back to Login", fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }
