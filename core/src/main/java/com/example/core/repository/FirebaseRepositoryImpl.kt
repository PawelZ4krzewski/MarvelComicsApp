package com.example.core.repository

import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseRepositoryImpl: FirebaseRepository {

    override fun createNewUser(email: String, password: String): Flow<Boolean> = callbackFlow {
        Firebase.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            trySend(task.isSuccessful)
        }
        awaitClose { }
    }

    override fun signInUser(email: String, password: String): Flow<Boolean> = callbackFlow {
        Firebase.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            trySend(task.isSuccessful)
        }
        awaitClose {  }
    }

    override fun signInGoogle(
        idToken: String?,
        onSuccess: () -> Unit
    ){
        val credentials = GoogleAuthProvider.getCredential(idToken, null)
        Firebase.auth.signInWithCredential(credentials).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            }
        }
    }
}