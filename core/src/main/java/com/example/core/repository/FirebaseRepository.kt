package com.example.core.repository

import com.example.core.data.remote.firebase.ComicData
import com.example.core.data.remote.firebase.UserComicsData
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {

    fun createNewUser(email: String, password: String): Flow<Boolean>
    fun signInUser(email: String, password: String): Flow<Boolean>
    fun signInGoogle(idToken: String?, onSuccess: () -> Unit)
    fun updateDeleteOrCreateFavouriteComics(comicData: ComicData, oldData: UserComicsData?, isDelete: Boolean)
    fun getDataFromUser(userId: String): Flow<UserComicsData?>
}