package com.example.core.repository

import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {

    fun createNewUser(email: String, password: String): Flow<Boolean>
    fun signInUser(email: String, password: String): Flow<Boolean>
    fun signInGoogle(idToken: String?, onSuccess: () -> Unit)
}