package com.example.core.repository

import android.util.Log
import com.example.core.data.remote.api.ComicApi
import com.example.core.data.remote.responses.MarvelApiData
import retrofit2.HttpException
import javax.inject.Inject

class MarvelComicRepositoryImpl @Inject constructor(
    private val api: ComicApi
) : MarvelComicRepository {
    override suspend fun getMarvelComicList(limit: Int, offset: Int): MarvelApiData? {
        val response = api.getComics(limit, offset)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
        return response.body()
    }

    override suspend fun searchMarvelComic(title: String): MarvelApiData? {
        val response = api.searchComics(title)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
        return response.body()
    }

    override suspend fun getComicsById(id: Int): MarvelApiData? {
        Log.d("Repository", "Przed response")
        val response = api.getComicsById(id)
        Log.d("Repository", "$response")
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
        return response.body()
    }
}

