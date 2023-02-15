package com.example.marvelcomicsapp.model

data class ComicItem (
    val id: Int,
    val title: String,
    val description: String?,
    val urls: List<ComicUrl>,
    val thumbnail: Thumbnail,
    val images: List<Image>,
    val creators: CreatorList
)
