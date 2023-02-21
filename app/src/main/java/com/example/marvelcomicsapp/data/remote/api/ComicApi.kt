package com.example.marvelcomicsapp.data.remote.api

import com.example.marvelcomicsapp.data.remote.responses.MarvelApiData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ComicApi {

    @GET("comics")
    suspend fun getComics(
        @Query("limit") limit: Int, @Query("offset") offset: Int
    ): Response<MarvelApiData>

    @GET("comics")
    suspend fun searchComics(
        @Query("titleStartsWith") titleStartsWith: String
    ): Response<MarvelApiData>

    @GET("comics/{comicId}")
    suspend fun getComicsById(
        @Path("comicId") comicId: Int
    ): Response<MarvelApiData>
}