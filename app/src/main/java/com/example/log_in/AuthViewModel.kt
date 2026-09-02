package com.example.log_in

import android.os.Message
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth= FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
     val authState = _authState.asStateFlow()


    init {
        checkAuthStatus()
    }
    fun checkAuthStatus() {
        if (auth.currentUser==null)
        {
            _authState.value= AuthState.UnAuthenticated
        }
        else _authState.value= AuthState.Authenticated

    }

    fun logIn(email: String,password: String){
        if (email.isEmpty() || password.isEmpty()){
            _authState.value= AuthState.Error("Something Wrong")
        return}
        _authState.value= AuthState.Loading
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                task ->
                if (task.isSuccessful){_authState.value= AuthState.Authenticated }
                else _authState.value= AuthState.Error(task.exception?.message?:"Something Wrong")
            }
    }
    fun signUp(email: String,password: String){
        if (email.isEmpty() || password.isEmpty()){
            _authState.value= AuthState.Error("Something Wrong")
            return}
        _authState.value= AuthState.Loading
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                    task ->
                if (task.isSuccessful){_authState.value= AuthState.Authenticated }
                else _authState.value= AuthState.Error(task.exception?.message?:"Something Wrong")
            }
    }
    fun signOut(){
        auth.signOut()
        _authState.value= AuthState.UnAuthenticated
    }
    sealed class AuthState(){

        object Authenticated: AuthState()
        object UnAuthenticated: AuthState()
        object Loading: AuthState()
        data class Error(val message: String): AuthState()
    }

}