package com.example.marvelcomicsapp.repository

import android.util.Log
import com.example.marvelcomicsapp.data.remote.api.ComicApi
import com.example.marvelcomicsapp.data.remote.responses.MarvelApiData
import retrofit2.HttpException
import javax.inject.Inject

class MarvelComicRepository @Inject constructor(
    private val api: ComicApi
) {
    suspend fun getMarvelComicList(limit: Int, offset: Int): MarvelApiData? {
        val response = api.getComics(limit, offset)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
        return response.body()
    }

    suspend fun searchMarvelComic(title: String): MarvelApiData? {
        val response = api.searchComics(title)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
        return response.body()
    }

    suspend fun getComicsById(id: Int): MarvelApiData? {
        Log.d("Repository", "Przed response")
        val response = api.getComicsById(id)
        Log.d("Repository", "$response")
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
        return response.body()
    }
}