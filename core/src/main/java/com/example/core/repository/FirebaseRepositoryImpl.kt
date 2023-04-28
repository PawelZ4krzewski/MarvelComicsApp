package com.example.core.repository

import com.example.core.data.remote.firebase.ComicData
import com.example.core.data.remote.firebase.UserComicsData
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseRepositoryImpl: FirebaseRepository {

    companion object{
        const val COLLECTION_NAME = "MarvelComics"
    }


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


    override fun updateDeleteOrCreateFavouriteComics(
        comicData: ComicData,
        oldData: UserComicsData?,
        isDelete: Boolean
    ){

        if(oldData != null){

            val newComicsList = if(isDelete){
                oldData.copy(
                    comics = oldData.comics.filter { it.comicId != comicData.comicId }
                )
            }else{
                oldData.copy(
                    comics = oldData.comics + comicData
                )
            }

            FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
                .document(Firebase.auth.uid ?: "-1")
                .set(newComicsList)
        } else{
            FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
                .document(Firebase.auth.uid ?: "-1")
                .set(UserComicsData(Firebase.auth.uid ?: "-1", listOf(comicData)))
        }
    }

    override fun getDataFromUser(userId: String): Flow<UserComicsData?> = callbackFlow{

        FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
            .document(userId)
            .get()
            .addOnSuccessListener {
                trySend(it.toObject(UserComicsData::class.java))
            }
            .addOnFailureListener{
                trySend(null)
            }

            awaitClose {  }
    }

}