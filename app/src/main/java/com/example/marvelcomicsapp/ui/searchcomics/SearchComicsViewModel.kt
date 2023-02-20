package com.example.marvelcomicsapp.ui.searchcomics

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelcomicsapp.data.remote.responses.Result
import com.example.marvelcomicsapp.repository.MarvelComicRepository
import com.example.marvelcomicsapp.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

data class SearchComicsState(
    val searchComicText: String = "",
    val searchComicHint: String = "Search for a comic book",
    val isSearchComicHintVisible: Boolean = true,

    val isSearched: Boolean = false,
    val comicBooks: List<Result> = emptyList(),
    val isFoundComics : Boolean = false
)

@HiltViewModel
class SearchComicsViewModel @Inject constructor(
    private val repository: MarvelComicRepository
): ViewModel() {

    private val _state: MutableState<SearchComicsState> = mutableStateOf(SearchComicsState())
    val state : MutableState<SearchComicsState> = _state


    fun searchComicsBook(query: String){
        viewModelScope.launch(Dispatchers.Default) {
            if(query.isNotEmpty()){
                try {
                    val result = repository.searchMarvelComic(query)

                    if(result != null){
                        _state.value = state.value.copy(
                            comicBooks = result.data.results,
                            isSearched = true,
                            isFoundComics = result.data.results.isNotEmpty()
                            )
                        Log.d("SearchComicsViewModel", "Correct download data")
                    }
                }catch (e: HttpException){
                    Log.d("SearchComicsViewModel", e.toString())
                }
            }
        }
    }


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
                searchComicsBook(state.value.searchComicText)
                Log.d("SearchComicsViewModel", state.value.searchComicText)
            }
        }
    }
}