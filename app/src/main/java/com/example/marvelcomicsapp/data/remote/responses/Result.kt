package com.example.marvelcomicsapp.data.remote.responses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result(
    val creators: Creators,
    val description: String?,
    val id: Int,
    val images: List<Image>,
    val thumbnail: Thumbnail,
    val title: String,
    val urls: List<Url>,
) : Parcelable