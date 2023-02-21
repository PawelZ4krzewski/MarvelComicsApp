package com.example.marvelcomicsapp.ui.comiclist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelcomicsapp.data.remote.responses.Result
import com.example.marvelcomicsapp.repository.MarvelComicRepository
import com.example.marvelcomicsapp.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

data class ComicListState(
    val comicBooks: List<Result> = emptyList(),
    val loadError: String = "",
    val isLoading: Boolean = false,
    val currentPage: Int = 0,
    val endReached: Boolean = false
)

@HiltViewModel
class ComicListViewModel @Inject constructor(
    private val repository: MarvelComicRepository
) : ViewModel() {

    private val _state = mutableStateOf(ComicListState())
    val state: State<ComicListState> = _state

    init {
        loadComicsPaginated()
    }

    fun loadComicsPaginated() {
        viewModelScope.launch {

            try {
                val result = repository.getMarvelComicList(
                    Constants.PAGE_SIZE,
                    state.value.currentPage * Constants.PAGE_SIZE
                )

                _state.value = state.value.copy(
                    comicBooks = state.value.comicBooks + result!!.data.results,
                    endReached = state.value.currentPage * Constants.PAGE_SIZE >= result.data.total,
                    currentPage = state.value.currentPage + 1
                )

            } catch (e: HttpException) {
                _state.value = state.value.copy(
                    loadError = e.toString()
                )
            }
        }
    }
}