package com.example.feature_main.ui.comiclist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.remote.firebase.ComicData
import com.example.core.data.remote.responses.FavResult
import com.example.core.repository.FirebaseRepository
import com.example.core.repository.MarvelComicRepositoryImpl
import com.example.core.util.Constants
import com.example.core.util.GoogleLoginManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

data class ComicListState(
    val comicBooks: List<FavResult> = emptyList(),
    val loadError: String = "",
    val isLoading: Boolean = false,
    val currentPage: Int = 0,
    val endReached: Boolean = false
)

@HiltViewModel
class ComicListViewModel @Inject constructor(
    private val repository: MarvelComicRepositoryImpl,
    private val repositoryFirebase: FirebaseRepository,
    private val googleLoginManager: GoogleLoginManager,
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
                    comicBooks = state.value.comicBooks + result!!.data.results.map { comics -> FavResult(result = comics, isFavourite = false)},
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

    fun changeFavComics(comicData: ComicData, userId: String, isDelegate: Boolean){
        viewModelScope.launch {
            repositoryFirebase.getDataFromUser(userId).collectLatest {
                repositoryFirebase.updateDeleteOrCreateFavouriteComics(comicData, it, isDelegate)
            }
        }
    }

    fun onEvent(event: ComicListEvent) {
        when (event) {
            is ComicListEvent.Logout -> {
                event.activity?.let { googleLoginManager.initGoogleManager(it) }
                googleLoginManager.logOut()
            }
            is ComicListEvent.AddComicsToFavourite -> {
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


            }
        }
    }
}