package com.example.marvelcomicsapp.data.remote.responses

data class Events(
    val available: Int,
    val collectionURI: String,
    val items: List<Any>,
    val returned: Int
)