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
import com.example.core.data.remote.firebase.ComicData
import com.example.core.data.remote.responses.FavResult
import com.example.core.repository.FirebaseRepository
import com.example.core.repository.MarvelComicRepositoryImpl
import com.example.feature_main.util.Constants.Companion.COMIC_TEXT_KEY
import com.example.feature_main.util.Constants.Companion.SEARCH_COMICS_KEY
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchComicsState(
    val searchComicText: String = "",
    val searchComicHint: String = "Search for a comic book",
    val isSearchComicHintVisible: Boolean = true,

    val isSearched: Boolean = false,
    val comicBooks: List<FavResult> = emptyList(),
    val isFoundComics: Boolean = false
)

@HiltViewModel
class SearchComicsViewModel @Inject constructor(
    private val repository: MarvelComicRepositoryImpl,
    private val repositoryFirebase: FirebaseRepository,

    app: Application,
) : AndroidViewModel(app) {

    private val currentUserId = Firebase.auth.currentUser?.uid ?: "-1"

    private val pref: SharedPreferences =
        app.getSharedPreferences("SearchVMSharedPrefs", Context.MODE_PRIVATE)
    private val editor = pref.edit()

    private val gson = Gson()

    private val _state: MutableState<SearchComicsState> = mutableStateOf(SearchComicsState())
    val state: State<SearchComicsState> = _state

    init {

        val comicsText = pref.getString(COMIC_TEXT_KEY, "") ?: ""
        val prefsComics = gson.fromJson<List<FavResult>>(pref.getString(SEARCH_COMICS_KEY, ""), object : TypeToken<List<FavResult>>() {}.type)

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
               kotlin.runCatching {

                    val result = repository.searchMarvelComic(query)
                    if (result != null) {
                        repositoryFirebase.getDataFromUser(currentUserId).collectLatest { userComicsData ->

                            _state.value = state.value.copy(
                                comicBooks = result.data.results.map { comics ->
                                    FavResult(
                                        result = comics,
                                        isFavourite = !userComicsData?.comics?.filter { it.comicId == comics.id.toString()}.isNullOrEmpty()
                                    )
                                },
                                isSearched = true,
                                isFoundComics = result.data.results.isNotEmpty()
                            )
                        }
                        editor.putString(SEARCH_COMICS_KEY, gson.toJson(state.value.comicBooks)).commit()
                    }
                }.onFailure {
                       Log.e("SearchComicsViewModel", it.message.toString())
               }
            }else {
                _state.value = state.value.copy(
                    comicBooks = listOf(),
                    isSearched = false,
                )
            }
        }
    }

    fun changeFavComics(comicData: ComicData, userId: String, isDelegate: Boolean){
        viewModelScope.launch {
            repositoryFirebase.getDataFromUser(userId).collectLatest {
                repositoryFirebase.updateDeleteOrCreateFavouriteComics(comicData, it, isDelegate)
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
            is SearchComicsEvent.AddComicsToFavourite -> {

                _state.value = state.value.copy(
                    comicBooks = state.value.comicBooks.map { favResult ->
                        if(favResult.result.id == event.comicsId) {
                            favResult.isFavourite = !favResult.isFavourite

                            changeFavComics(
                                comicData = ComicData(
                                    comicId = favResult.result.id.toString(),
                                    authors = favResult.result.creators.items.joinToString { creator -> creator.name },
                                    description = favResult.result.description ?: "",
                                    image = favResult.result.images?.firstOrNull()?.let { "${it.path}.${it.extension}" },
                                    title = favResult.result.title,
                                    url = favResult.result.urls.firstOrNull()?.url ?: ""
                                ),
                                userId = Firebase.auth.currentUser?.uid ?: "-1",
                                isDelegate = !favResult.isFavourite
                            )

                        }
                        favResult
                    }
                )
                editor.putString(SEARCH_COMICS_KEY, gson.toJson(state.value.comicBooks)).commit()

            }
        }
    }
}