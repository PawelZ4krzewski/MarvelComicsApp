package com.example.marvelcomicsapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val path: String,
    val extenstion: String
): Parcelable
