package com.example.feature_auth.fragments.login

import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import com.example.core.repository.FirebaseRepository
import com.example.core.util.GoogleLoginManager
import com.example.feature_auth.LoginActivity
import com.example.feature_auth.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
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
        val isEmailCorrect = loginState.value.email.trim().isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(loginState.value.email).matches()
        return isEmailCorrect && loginState.value.password.trim().isNotBlank() && loginState.value.password.count() >= Constants.MIN_PASSWORD_CHAR_AMOUNT
        }
    fun singIn() {
        isLoginSuccesfull = repository.signInUser(loginState.value.email, loginState.value.password, auth)
    }

    fun initGoogleManager(activity: LoginActivity) = googleLoginManager.initGoogleManager(activity)
    fun signInGoogle(token: String?, onSuccess: () -> Unit) {
        repository.signInGoogle(token, auth, onSuccess)
    }
    fun getGoogleClient() = googleLoginManager.getClient()
    fun getSignedInAccountFromIntent(result: ActivityResult) = googleLoginManager.getSignedInAccountFromIntent(result)

}