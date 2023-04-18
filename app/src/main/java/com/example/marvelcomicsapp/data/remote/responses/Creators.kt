package com.example.marvelcomicsapp.data.remote.responses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Creators(
    val items: List<CreatorItem>,
    val returned: Int
) : Parcelable