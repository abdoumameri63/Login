package com.example.log_in

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState = _authState.asStateFlow()

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        val user = auth.currentUser
        _authState.value = when {
            user == null -> AuthState.UnAuthenticated
            !user.isEmailVerified -> AuthState.EmailNotVerified
            else -> AuthState.Authenticated
        }
    }

    fun logIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Please fill in all fields")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    _authState.value = if (user?.isEmailVerified == true) {
                        AuthState.Authenticated
                    } else {
                        AuthState.EmailNotVerified
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun signUp(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Please fill in all fields")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _authState.value = AuthState.Error("Please enter a valid email address")
            return
        }
        if (password.length < 6) {
            _authState.value = AuthState.Error("Password must be at least 6 characters")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener {
                            _authState.value = AuthState.EmailNotVerified
                        }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    // Call this when user taps "I've verified my email" on the verify screen
    fun refreshEmailVerificationStatus() {
        val user = auth.currentUser ?: run {
            _authState.value = AuthState.UnAuthenticated
            return
        }
        user.reload().addOnCompleteListener {
            _authState.value = if (user.isEmailVerified) {
                AuthState.Authenticated
            } else {
                AuthState.Error("Still not verified — check your inbox (and spam folder)")
            }
        }
    }

    fun resendVerificationEmail() {
        auth.currentUser?.sendEmailVerification()
    }

    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.UnAuthenticated
    }
    fun sendPasswordReset(email: String) {
        if (email.isEmpty()) {
            _authState.value = AuthState.Error("Please enter your email")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _authState.value = AuthState.Error("Please enter a valid email address")
            return
        }
        _authState.value = AuthState.Loading
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                _authState.value = if (task.isSuccessful) {
                    AuthState.PasswordResetSent
                } else {
                    AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }
    fun resetToUnauthenticated() {
        _authState.value = AuthState.UnAuthenticated
    }

    sealed class AuthState {
        object Authenticated : AuthState()
        object UnAuthenticated : AuthState()
        object EmailNotVerified : AuthState()

        object PasswordResetSent : AuthState()

        object Loading : AuthState()
        data class Error(val message: String) : AuthState()
    }
}