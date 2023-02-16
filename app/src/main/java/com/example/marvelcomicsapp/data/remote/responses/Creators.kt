package com.example.marvelcomicsapp.data.remote.responses

data class Creators(
    val available: Int,
    val collectionURI: String,
    val items: List<CreatorItem>,
    val returned: Int
)