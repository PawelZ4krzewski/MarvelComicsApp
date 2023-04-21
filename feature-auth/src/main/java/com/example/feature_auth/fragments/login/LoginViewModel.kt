package com.example.feature_auth.fragments.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow


class LoginViewModel: ViewModel() {

    private val _username = MutableStateFlow("")
    private val _password = MutableStateFlow("")

    fun setUsername(username: String){
        _username.value = username
    }
    fun setPassword(password: String){
        _password.value = password
    }
}