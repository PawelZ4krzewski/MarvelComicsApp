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

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    var isLoginSuccesfull: Flow<Boolean> = flowOf(false)

    fun checkLoginData(): Boolean {
        val isEmailCorrect = email.value.trim().isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
        return isEmailCorrect && password.value.trim().isNotBlank()
    }
    fun singIn() {
        isLoginSuccesfull = repository.signInUser(email.value, password.value, auth)
    }
}