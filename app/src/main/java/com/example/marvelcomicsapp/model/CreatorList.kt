package com.example.marvelcomicsapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreatorList(
    val returned: Int,
    val collectionURI: String,
    val creators: List<CreatorItem>
) : Parcelable
