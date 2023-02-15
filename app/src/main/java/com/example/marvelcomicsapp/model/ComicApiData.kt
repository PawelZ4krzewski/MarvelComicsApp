package com.example.marvelcomicsapp.model

data class ComicApiData(
    val code: Int,
    val stauts: String,
    val copyright: String,
    val attributionText: String,
    val attributionHTML: String,
    val etag: String,
    val data: ComicData
)
