package com.example.feature_main.ui.favcomics

import com.example.core.data.remote.firebase.ComicData

sealed class FavComicsEvent{
    data class AddComicsToFavourite(val comicData: ComicData): FavComicsEvent()
}
