package com.example.marvelcomicsapp.repository

import com.example.marvelcomicsapp.data.remote.responses.MarvelApiData

interface MarvelComicRepositoryInterface {
    suspend fun getMarvelComicList(limit: Int, offset: Int): MarvelApiData?
    suspend fun searchMarvelComic(title: String): MarvelApiData?
    suspend fun getComicsById(id: Int): MarvelApiData?
}