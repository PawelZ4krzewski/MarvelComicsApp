package com.example.feature_auth.fragments.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.core.repository.FirebaseRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: FirebaseRepositoryImpl,
    private val auth: FirebaseAuth
): ViewModel() {

    private val _username = MutableStateFlow("")
    private val _password = MutableStateFlow("")

    var isLoginSuccesfull: Flow<Boolean> = flowOf(false)

    fun setUsername(username: String){
        _username.value = username
    }
    fun setPassword(password: String){
        _password.value = password
    }

    fun singIn() {
        isLoginSuccesfull = repository.signInUser(_username.value, _password.value, auth)
    }
}