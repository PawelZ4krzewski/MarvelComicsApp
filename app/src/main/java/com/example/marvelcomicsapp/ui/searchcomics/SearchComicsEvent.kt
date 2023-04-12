package com.example.marvelcomicsapp.ui.searchcomics

import androidx.compose.ui.focus.FocusState

sealed class SearchComicsEvent {
    data class EnterText(val text: String) : SearchComicsEvent()
    data class ChangeText(val focusState: FocusState) : SearchComicsEvent()

    object SearchComics : SearchComicsEvent()
    object CancelSearching : SearchComicsEvent()
}
