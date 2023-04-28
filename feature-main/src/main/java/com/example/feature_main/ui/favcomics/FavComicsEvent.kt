package com.example.feature_main.ui.favcomics

sealed class FavComicsEvent{
    data class AddComicsToFavourite(val comicsId: Int): FavComicsEvent()
}
