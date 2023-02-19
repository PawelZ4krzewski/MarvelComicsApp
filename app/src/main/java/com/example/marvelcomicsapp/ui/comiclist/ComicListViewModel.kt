package com.example.marvelcomicsapp.ui.comiclist

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marvelcomicsapp.data.remote.responses.MarvelApiData
import com.example.marvelcomicsapp.repository.MarvelComicRepository
import com.example.marvelcomicsapp.data.remote.responses.Result
import com.example.marvelcomicsapp.model.ComicApiData
import com.example.marvelcomicsapp.model.ComicItem
import com.example.marvelcomicsapp.util.Constants
import com.example.marvelcomicsapp.util.Resource
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
): ViewModel() {

    private val _state = mutableStateOf(ComicListState())
    val state: State<ComicListState> = _state


    init {
        loadComicsPaginated()
    }

//    var comicsLiveData = MutableLiveData<MarvelApiData?>()

//    fun getComics(){
//        viewModelScope.launch {
//
//            runCatching { repository.getMarvelComicList() }
//                .onSuccess {
//                    comicsLiveData.value = it
//                    Log.d("ComicListViewModel", "Udalo sie pobrac")
//                    Log.d("ComicListViewModel", it.toString())
//                }
//                .onFailure {
//                    Log.d("ComicListViewModel", it.toString())
//                }
//        }
//    }

    fun loadComicsPaginated() {
        viewModelScope.launch {

            try {
                val result = repository.getMarvelComicList(
                    Constants.PAGE_SIZE,
                    state.value.currentPage * Constants.PAGE_SIZE)


                _state.value = state.value.copy(
                    comicBooks = state.value.comicBooks + result!!.data.results,
                    endReached = state.value.currentPage * Constants.PAGE_SIZE >= result.data.results.count(),
                    currentPage = state.value.currentPage + 1
                )
                Log.d("ComicListViewModel", "Correct download data")
                Log.d("ComicListViewModel", state.value.toString())

            }catch (e: HttpException){
                _state.value = state.value.copy(
                    loadError = e.toString()
                )
                Log.d("ComicListViewModel", e.toString())
            }
        }
    }
}