package com.example.core.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseRepositoryImpl: FirebaseRepository {

    override fun createNewUser(email: String, password: String, auth: FirebaseAuth): Flow<Boolean> = callbackFlow {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            trySend(task.isSuccessful)
        }
        awaitClose { }
    }

    override fun signInUser(email: String, password: String, auth: FirebaseAuth): Flow<Boolean> = callbackFlow {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            trySend(task.isSuccessful)
        }
        awaitClose {  }
    }

    override fun signInGoogle(
        idToken: String?,
        auth: FirebaseAuth,
        onSuccess: () -> Unit
    ){
        val credentials = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credentials).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            }
        }
    }
}