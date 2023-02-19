package com.example.marvelcomicsapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ComicUrl(
    val type: String,
    val url: String
) : Parcelable
