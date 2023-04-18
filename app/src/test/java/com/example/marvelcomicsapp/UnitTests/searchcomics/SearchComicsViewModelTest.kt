package com.example.marvelcomicsapp.UnitTests.searchcomics

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.example.marvelcomicsapp.data.remote.responses.*
import com.example.marvelcomicsapp.repository.MarvelComicRepository
import com.example.marvelcomicsapp.ui.searchcomics.SearchComicsEvent
import com.example.marvelcomicsapp.ui.searchcomics.SearchComicsState
import com.example.marvelcomicsapp.ui.searchcomics.SearchComicsViewModel
import com.example.marvelcomicsapp.util.MainCoroutineRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import strikt.api.expectThat
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isTrue

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class SearchComicsViewModelTest{

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: SearchComicsViewModel

    @MockK
    private var fakeResult = mockk<Result>(relaxed = true)

    @MockK
    private var fakeData = mockk<Data>(relaxed = true)

    @MockK
    private var fakeMarvelApiData = mockk<MarvelApiData>(relaxed = true)

    @MockK
    private var fakeMarvelRepository = mockk<MarvelComicRepository>()

    @Before
    fun setup(){

        fakeResult = fakeResult.copy(
            creators = Creators(listOf(CreatorItem("Adam"),CreatorItem("Jacob")),2),
            title = "Spider-man",
            description = "Description"
        )


        fakeData = fakeData.copy(
             results  =  listOf(fakeResult, fakeResult, fakeResult)
        )

        fakeMarvelApiData = fakeMarvelApiData.copy(
             data = fakeData
        )


        with(fakeMarvelRepository){
            coEvery {
                getMarvelComicList(2,0)
            } returns fakeMarvelApiData

            coEvery {
                searchMarvelComic("Spider")
            } returns fakeMarvelApiData
        }

        viewModel = SearchComicsViewModel(fakeMarvelRepository, Application())
    }

    @Test
    fun `WHEN title start with Spider RETURN Spider-man comics`() = runTest {
        viewModel.searchComicsBook("Spider")
        advanceUntilIdle()
        expectThat(viewModel.state.value){
            get(SearchComicsState::comicBooks).isEqualTo(fakeMarvelApiData.data.results)
            get(SearchComicsState::isSearched).isTrue()
        }
    }

    @Test
    fun `WHEN user type text THEN stage save it`() = runTest{
        val tekst = "Test"
        viewModel.onEvent(SearchComicsEvent.EnterText(tekst))
        assertEquals(tekst, viewModel.state.value.searchComicText)
    }

    @Test
    fun `WHEN user click THEN comiclist is empty and isSearched is false and hint is visible`() = runTest {

        viewModel.onEvent(SearchComicsEvent.CancelSearching)

        expectThat(viewModel.state.value){
            get(SearchComicsState::comicBooks).isEmpty()
            get(SearchComicsState::isSearched).isFalse()
            get(SearchComicsState::isSearchComicHintVisible).isTrue()
        }

    }
}
