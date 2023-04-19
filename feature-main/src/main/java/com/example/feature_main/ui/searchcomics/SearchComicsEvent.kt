package com.example.feature_main.ui.searchcomics

import androidx.compose.ui.focus.FocusState

sealed class SearchComicsEvent {
    data class EnterText(val text: String) : SearchComicsEvent()
    object ChangeText : SearchComicsEvent()

    object SearchComics : SearchComicsEvent()
    object CancelSearching : SearchComicsEvent()
}
