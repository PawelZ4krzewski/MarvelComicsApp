package com.example.core.data.remote.firebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserComicsData(
    var userId: String = "-1",
    var comics: List<ComicData> = listOf()
) : Parcelable
