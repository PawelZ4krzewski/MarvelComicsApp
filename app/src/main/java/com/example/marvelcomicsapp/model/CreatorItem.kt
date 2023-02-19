package com.example.marvelcomicsapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreatorItem(
    val name: String,
    val role: String
): Parcelable
