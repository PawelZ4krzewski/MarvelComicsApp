package com.example.marvelcomicsapp.data.remote.responses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Thumbnail(
    val extension: String,
    val path: String
) : Parcelable