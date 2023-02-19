package com.example.marvelcomicsapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ComicData (
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<ComicItem>
) : Parcelable
