package com.example.feature_main.ui.searchcomics

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.remote.responses.Result
import com.example.core.repository.MarvelComicRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

data class SearchComicsState(
    val searchComicText: String = "",
    val searchComicHint: String = "Search for a comic book",
    val isSearchComicHintVisible: Boolean = true,

    val isSearched: Boolean = false,
    val comicBooks: List<Result> = emptyList(),
    val isFoundComics: Boolean = false
)

@HiltViewModel
class SearchComicsViewModel @Inject constructor(
    private val repository: MarvelComicRepository,
    app: Application,
) : AndroidViewModel(app) {

    private val COMIC_TEXT_KEY = "ComicTextKEY"
    private val SEARCH_COMICS_KEY = "SearchComicListKEY"

    private val pref: SharedPreferences =
        app.getSharedPreferences("SearchVMSharedPrefs", Context.MODE_PRIVATE)
    private val editor = pref.edit()

    private val gson = Gson()

    private val _state: MutableState<SearchComicsState> = mutableStateOf(SearchComicsState())
    val state: State<SearchComicsState> = _state


    init {
        val comicsText = pref.getString(COMIC_TEXT_KEY, "") ?: ""
        val prefsComics = gson.fromJson<List<Result>>(pref.getString(SEARCH_COMICS_KEY, ""), object : TypeToken<List<Result>>() {}.type)

        _state.value = state.value.copy(
            searchComicText = comicsText,
            comicBooks = prefsComics ?: listOf(),
            isSearchComicHintVisible = comicsText.isBlank(),
            isSearched = comicsText.isNotBlank(),
            isFoundComics = !prefsComics.isNullOrEmpty()
        )

    }

    fun searchComicsBook(query: String) {
        viewModelScope.launch {
            if (query.isNotEmpty()) {
                try {
                    val result = repository.searchMarvelComic(query)
                    if (result != null) {
                        _state.value = state.value.copy(
                            comicBooks = result.data.results,
                            isSearched = true,
                            isFoundComics = result.data.results.isNotEmpty()
                        )
                        editor.putString(SEARCH_COMICS_KEY, gson.toJson(state.value.comicBooks)).commit()
                    }
                } catch (e: HttpException) {
                    Log.e("SearchComicsViewModel", e.toString())
                }
            }else {
                _state.value = state.value.copy(
                    comicBooks = listOf(),
                    isSearched = false,
                )
            }
        }
    }


    fun onEvent(event: SearchComicsEvent) {
        when (event) {
            is SearchComicsEvent.EnterText -> {
                _state.value = state.value.copy(
                    searchComicText = event.text
                )
                editor.putString(COMIC_TEXT_KEY, event.text).commit()
            }
            is SearchComicsEvent.ChangeText -> {
                _state.value = state.value.copy(
                    isSearchComicHintVisible = state.value.searchComicText.isBlank()
                )
            }
            is SearchComicsEvent.SearchComics -> {
                searchComicsBook(state.value.searchComicText)
                Log.d("SearchComicsViewModel", state.value.searchComicText)
            }
            is SearchComicsEvent.CancelSearching -> {
                _state.value = state.value.copy(
                    comicBooks = listOf(),
                    isSearched = false,
                    isSearchComicHintVisible = true
                )
            }
        }
    }
}