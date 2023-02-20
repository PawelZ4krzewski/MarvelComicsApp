package com.example.marvelcomicsapp.repository

import android.util.Log
import com.example.marvelcomicsapp.data.remote.api.ComicApi
import com.example.marvelcomicsapp.data.remote.responses.MarvelApiData
import com.example.marvelcomicsapp.model.ComicApiData
import com.example.marvelcomicsapp.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.HttpException
import javax.inject.Inject

@ActivityScoped
class MarvelComicRepository @Inject constructor(
    private val api : ComicApi
){
//    Probably not MarvelApiData but just Data
//    suspend fun getMarvelComicList(limit: Int, offset : Int): Resource<ComicApiData> {
//    suspend fun getMarvelComicList(): Resource<ComicApiData> {
//        val response = try{
//            api.getComics()
////            api.getComics(limit, offset)
//        } catch(e: java.lang.Exception){
//            return Resource.Error("An unknown error occured. $e")
//        }
//
//        return Resource.Success(response)
//    }

    suspend fun getMarvelComicList(limit: Int, offset : Int): MarvelApiData? {
        val response = api.getComics(limit, offset)
        if (!response.isSuccessful){
            throw HttpException(response)
        }
        return response.body()
    }

    suspend fun searchMarvelComic(title: String): MarvelApiData? {
        val response = api.searchComics(title)
        if (!response.isSuccessful){
            throw HttpException(response)
        }
        return response.body()
    }

    suspend fun getComicsById(id: Int): MarvelApiData? {
        Log.d("Repository", "Przed response")
        val response = api.getComicsById(id)
        Log.d("Repository", "$response")
        if (!response.isSuccessful){
            throw HttpException(response)
        }
        return response.body()
    }
}