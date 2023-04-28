package com.example.core.data.remote.firebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ComicData (
    var comicId: String = "",
    var authors: String = "",
    var description: String = "",
    var image: String? = null,
    var title: String = "",
    var url: String = "",
) : Parcelable