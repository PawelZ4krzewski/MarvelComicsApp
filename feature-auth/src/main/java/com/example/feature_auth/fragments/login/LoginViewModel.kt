package com.example.feature_auth.fragments.login

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

    private val _email = MutableStateFlow("")
    private val _password = MutableStateFlow("")

    var isLoginSuccesfull: Flow<Boolean> = flowOf(false)

    fun setUsername(email: String){
        _email.value = email
    }
    fun setPassword(password: String){
        _password.value = password
    }

    fun checkLoginData(): Boolean {
        val isEmailCorrect = _email.value.trim().isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()
        return isEmailCorrect && _password.value.trim().isNotBlank()
    }
    fun singIn() {
        isLoginSuccesfull = repository.signInUser(_email.value, _password.value, auth)
    }
}