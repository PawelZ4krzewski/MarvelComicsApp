package com.example.marvelcomicsapp.data.remote.api

import com.example.marvelcomicsapp.data.remote.responses.MarvelApiData
import com.example.marvelcomicsapp.model.ComicApiData
import com.example.marvelcomicsapp.model.ComicUrl
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ComicApi {

    @GET("comics")
    suspend fun getComics(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<MarvelApiData>
}