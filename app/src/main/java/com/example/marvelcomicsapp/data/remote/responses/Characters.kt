package com.example.marvelcomicsapp.data.remote.responses

data class Characters(
    val available: Int,
    val collectionURI: String,
    val items: List<CharakterItem>,
    val returned: Int
)