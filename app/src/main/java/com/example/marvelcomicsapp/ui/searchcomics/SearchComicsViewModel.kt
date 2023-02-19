package com.example.marvelcomicsapp.ui.searchcomics

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.marvelcomicsapp.repository.MarvelComicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class SearchComicsState(
    val searchComicText: String = "",
    val searchComicHint: String = "Search for a comic book",
    val isSearchComicHintVisible: Boolean = false,
)

@HiltViewModel
class SearchComicsViewModel @Inject constructor(
    private val repository: MarvelComicRepository
): ViewModel() {

    private val _state: MutableState<SearchComicsState> = mutableStateOf(SearchComicsState())
    val state : MutableState<SearchComicsState> = _state


    fun onEvent(event: SearchComicsEvent){
        when(event){
            is SearchComicsEvent.EnterText -> {
                _state.value = state.value.copy(
                    searchComicText = event.text
                )
            }
            is SearchComicsEvent.ChangeText -> {
                _state.value = state.value.copy(
                    isSearchComicHintVisible = !event.focusState.isFocused && state.value.searchComicText.isBlank()
                )
            }
            SearchComicsEvent.SearchComics -> {
                Log.d("SearchComicsViewModel", state.value.searchComicText)
            }
        }
    }
}