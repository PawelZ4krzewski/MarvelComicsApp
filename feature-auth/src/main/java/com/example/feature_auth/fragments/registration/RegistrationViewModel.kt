package com.example.feature_auth.fragments.registration

import androidx.lifecycle.ViewModel
import com.example.core.repository.FirebaseRepository
import com.example.feature_auth.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val repository: FirebaseRepository,
): ViewModel() {

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val repeatPassword = MutableStateFlow("")

    var isRegistrationSuccesfull: Flow<Boolean> = flowOf(false)

    fun checkLoginData(): Boolean {
        val isEmailCorrect = email.value.trim().isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
        val isPasswordsAreIdentical = password.value == repeatPassword.value
        val isPasswordEnoughLong = password.value.count() >= Constants.MIN_PASSWORD_CHAR_AMOUNT && repeatPassword.value.count() >= Constants.MIN_PASSWORD_CHAR_AMOUNT
        return isEmailCorrect && password.value.trim().isNotBlank() && repeatPassword.value.trim().isNotBlank() && isPasswordsAreIdentical && isPasswordEnoughLong
    }

    fun createUser() {
        isRegistrationSuccesfull = repository.createNewUser(email.value, password.value)
    }
}