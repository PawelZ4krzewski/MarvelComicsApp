package com.example.feature_main.ui.comicsdetails

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.repository.MarvelComicRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.core.data.remote.responses.Result

data class ComicsDetailsState(
    val comicBook: Result? = null
)

@HiltViewModel
class ComicsDetailsViewModel @Inject constructor(
    private val repository: MarvelComicRepositoryImpl,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(ComicsDetailsState())
    val state: State<ComicsDetailsState> = _state

    init {
        savedStateHandle.get<Int>("comicsBook")?.let { comicsBookId ->
            getComicsById(comicsBookId)
        }
    }


    private fun getComicsById(id: Int) {
        viewModelScope.launch {
            kotlin.runCatching { repository.getComicsById(id) }
                .onSuccess {
                    _state.value = state.value.copy(
                        comicBook = it!!.data.results.firstOrNull(),
                    )
                }
                .onFailure { e: Throwable ->
                    Log.e("ComicsDetailsViewModel", e.toString())
                }
        }
    }
}