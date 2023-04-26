package com.example.core.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {

    fun createNewUser(email: String, password: String, auth: FirebaseAuth): Flow<Boolean>
    fun signInUser(email: String, password: String, auth: FirebaseAuth): Flow<Boolean>
    fun signInGoogle(idToken: String?, auth: FirebaseAuth, onSuccess: () -> Unit)
}