package com.example.core.repository

import com.example.core.data.remote.responses.MarvelApiData


interface MarvelComicRepository {
    suspend fun getMarvelComicList(limit: Int, offset: Int): MarvelApiData?
    suspend fun searchMarvelComic(title: String): MarvelApiData?
    suspend fun getComicsById(id: Int): MarvelApiData?
}