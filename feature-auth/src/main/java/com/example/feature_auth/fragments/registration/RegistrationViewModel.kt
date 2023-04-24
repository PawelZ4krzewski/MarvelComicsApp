package com.example.feature_auth.fragments.registration

import androidx.lifecycle.ViewModel
import com.example.core.repository.FirebaseRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val repository: FirebaseRepositoryImpl,
    private val auth: FirebaseAuth
): ViewModel() {

    private val _email = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    private val _repeatPassword = MutableStateFlow("")

    var isRegistrationSuccesfull: Flow<Boolean> = flowOf(false)

    fun setUsername(email: String){
        _email.value = email
    }
    fun setPassword(password: String){
        _password.value = password
    }
    fun setRepeatPassword(password: String){
        _repeatPassword.value = password
    }

    fun checkLoginData(): Boolean {
        val isEmailCorrect = _email.value.trim().isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()
        val isPasswordsAreIdentical = _password.value == _repeatPassword.value
        return isEmailCorrect && _password.value.trim().isNotBlank() && _repeatPassword.value.trim().isNotBlank() && isPasswordsAreIdentical
    }

    fun createUser() {
        isRegistrationSuccesfull = repository.createNewUser(_email.value, _password.value, auth)
    }
}