package com.example.feature_main.ui.favcomics

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.remote.firebase.ComicData
import com.example.core.repository.FirebaseRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavComicsState(
    val favComicsBooks: List<ComicData> = listOf()
)


@HiltViewModel
class FavComicsViewModel @Inject constructor(
    private val repositoryFirebase: FirebaseRepository,
) : ViewModel() {

    private val _state = mutableStateOf(FavComicsState())
    val state: State<FavComicsState> = _state

    private val currentUserId = Firebase.auth.currentUser?.uid ?: "-1"

    init {
        getFavComics()
    }

    fun getFavComics(){
        viewModelScope.launch {
            repositoryFirebase.getDataFromUser(Firebase.auth.currentUser?.uid ?: "-1")
                .collectLatest { userComicsData ->
                    _state.value = state.value.copy(
                        favComicsBooks = userComicsData?.comics ?: emptyList(),
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
    fun onEvent(event: FavComicsEvent){
        when(event){
            is FavComicsEvent.AddComicsToFavourite -> {
                changeFavComics(event.comicData, currentUserId, true)

                _state.value = state.value.copy(
                    favComicsBooks = state.value.favComicsBooks.filter { it.comicId != event.comicData.comicId }
                )
            }
        }
    }
}