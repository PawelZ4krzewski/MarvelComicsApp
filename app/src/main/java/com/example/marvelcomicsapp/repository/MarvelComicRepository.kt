package com.example.marvelcomicsapp.repository

import com.example.marvelcomicsapp.data.remote.api.ComicApi
import com.example.marvelcomicsapp.data.remote.responses.Data
import com.example.marvelcomicsapp.data.remote.responses.MarvelApiData
import com.example.marvelcomicsapp.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class MarvelComicRepository @Inject constructor(
    private val api : ComicApi
){
//    Probably not MarvelApiData but just Data
    suspend fun getMarvelComicList(limit: Int, offet : Int): Resource<MarvelApiData> {
        val response = try{
            api.getComics(limit, offet)
        } catch(e: java.lang.Exception){
            return Resource.Error("An unknown error occured.")
        }

        return Resource.Success(response)
    }
}