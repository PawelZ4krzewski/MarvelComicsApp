package com.example.core.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseRepositoryImpl: FirebaseRepository {

    override fun createNewUser(email: String, password: String, auth: FirebaseAuth): Flow<Boolean> = callbackFlow {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                Log.d("CreatingUser", "createUserWithEmail:success")
                trySend(task.isSuccessful)
            } else {
                Log.d("CreatingUser", "createUserWithEmail: failure, ${task.exception}")
                trySend(!task.isSuccessful)
            }
        }
        awaitClose { }
    }



    override fun signInUser(email: String, password: String, auth: FirebaseAuth): Flow<Boolean> = callbackFlow {


        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("SignInUser", "signInWithEmail:success")
                trySend(task.isSuccessful)
            } else {
                Log.d("SignInUser", "signInWithEmail: failure, ${task.exception}")
                trySend(!task.isSuccessful)
            }
        }
        awaitClose {  }
    }
}