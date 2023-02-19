package com.example.marvelcomicsapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ComicApiData(
    val code: Int,
    val stauts: String,
    val copyright: String,
    val attributionText: String,
    val attributionHTML: String,
    val etag: String,
    val data: ComicData
): Parcelable
