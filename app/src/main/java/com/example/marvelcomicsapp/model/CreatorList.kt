package com.example.marvelcomicsapp.model

data class CreatorList(
    val returned: Int,
    val collectionURI: String,
    val creators: List<CreatorItem>
)
