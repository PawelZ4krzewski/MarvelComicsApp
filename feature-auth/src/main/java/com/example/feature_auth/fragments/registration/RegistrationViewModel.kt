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

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val repeatPassword = MutableStateFlow("")

    var isRegistrationSuccesfull: Flow<Boolean> = flowOf(false)

    fun checkLoginData(): Boolean {
        val isEmailCorrect = email.value.trim().isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
        val isPasswordsAreIdentical = password.value == repeatPassword.value
        return isEmailCorrect && password.value.trim().isNotBlank() && repeatPassword.value.trim().isNotBlank() && isPasswordsAreIdentical
    }

    fun createUser() {
        isRegistrationSuccesfull = repository.createNewUser(email.value, password.value, auth)
    }
}