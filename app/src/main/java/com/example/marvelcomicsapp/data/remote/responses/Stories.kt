package com.example.marvelcomicsapp.data.remote.responses

data class Stories(
    val available: Int,
    val collectionURI: String,
    val items: List<StoryItem>,
    val returned: Int
)