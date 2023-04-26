package com.example.feature_auth.fragments.login

import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import com.example.core.repository.FirebaseRepository
import com.example.core.util.GoogleLoginManager
import com.example.feature_auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class LoginState(
    var email: String = "",
    var password: String = "",
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: FirebaseRepository,
    private val googleLoginManager: GoogleLoginManager,
    private val auth: FirebaseAuth
): ViewModel() {


    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    var isLoginSuccesfull: Flow<Boolean> = flowOf(false)

    fun checkLoginData(): Boolean {
//        val isEmailCorrect = email.value.trim().isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
//        return isEmailCorrect && password.value.trim().isNotBlank()
        Log.d("DUPA","email: ${loginState.value.email} password: ${loginState.value.password}")
        return true
    }
    fun singIn() {
        isLoginSuccesfull = repository.signInUser(loginState.value.email, loginState.value.password, auth)
    }

    fun initGoogleManager(activity: LoginActivity) = googleLoginManager.initGoogleManager(activity)
    fun signInGoogle(token: String?, onSuccess: () -> Unit) {
        Log.d("DUPA", "IN signInGoogle in ViewModel")
        repository.signInGoogle(token, auth, onSuccess)
//        isLoginSuccesfull = repository.signInGoogle(token, auth, onSuccess)
    }
    fun getGoogleClient() = googleLoginManager.getClient()
    fun getSignedInAccountFromIntent(result: ActivityResult) = googleLoginManager.getSignedInAccountFromIntent(result)

}