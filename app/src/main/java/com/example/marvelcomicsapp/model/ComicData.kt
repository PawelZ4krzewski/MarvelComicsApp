package com.example.marvelcomicsapp.model

data class ComicData (
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val comics: List<ComicItem>
)
