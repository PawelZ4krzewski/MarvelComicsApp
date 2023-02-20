package com.example.marvelcomicsapp.ui.comicsdetails

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.marvelcomicsapp.repository.MarvelComicRepository
import com.example.marvelcomicsapp.data.remote.responses.Result
import com.example.marvelcomicsapp.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

data class ComicsDetailsState(
    val comicBook: Result? = null
)

@HiltViewModel
class ComicsDetailsViewModel @Inject constructor(
    private val repository: MarvelComicRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = mutableStateOf(ComicsDetailsState())
    val state: State<ComicsDetailsState> = _state

    init {
        savedStateHandle.get<Int>("comicsBook")?.let { comicsBookId ->
            Log.d("ComicsDetailsViewModel", "$comicsBookId")
            getComicsById(comicsBookId)
            Log.d("ComicsDetailsViewModel", "POBRANE ${state.value.comicBook}")

        }
    }


    private fun getComicsById(id: Int){
        viewModelScope.launch {
            kotlin.runCatching { repository.getComicsById(id) }
                .onSuccess {
                    _state.value = state.value.copy(
                    comicBook = it!!.data.results.firstOrNull(),
                    )
                }
                .onFailure {error: Throwable ->
                    Log.d("ComicsDetailsViewModel", "Error", error)
                }
        }
    }
}