package com.example.feature_main.ui.searchcomics

sealed class SearchComicsEvent {
    data class EnterText(val text: String) : SearchComicsEvent()
    data class AddComicsToFavourite(val comicsId: Int): SearchComicsEvent()

    object ChangeText : SearchComicsEvent()

    object SearchComics : SearchComicsEvent()
    object CancelSearching : SearchComicsEvent()
}
