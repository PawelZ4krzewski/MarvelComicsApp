package com.example.marvelcomicsapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ComicItem (
    val id: Int,
    val title: String,
    val description: String?,
    val urls: List<ComicUrl>,
    val thumbnail: Thumbnail,
    val images: List<Image>,
    val creators: CreatorList
) : Parcelable
