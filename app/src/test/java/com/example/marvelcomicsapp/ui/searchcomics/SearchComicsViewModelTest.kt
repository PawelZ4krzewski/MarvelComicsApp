package com.example.marvelcomicsapp.ui.searchcomics

import com.example.marvelcomicsapp.data.remote.responses.*
import com.example.marvelcomicsapp.repository.MarvelComicRepository
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
    private val fakeMarvelRepository = mockk<MarvelComicRepository>()

    @MockK
    private val fakeMarvelApiData = mockk<MarvelApiData>(relaxed = true)

    @MockK
    private val fakeData = mockk<Data>(relaxed = true)

    @MockK
    private val fakeResult = mockk<Result>(relaxed = true)

    @Before
    fun setup(){

        fakeResult.apply {
            every { creators } returns Creators(listOf(CreatorItem("Adam"),CreatorItem("Jacob")),2)
            every { title } returns "Spider-man"
            every { description } returns "Description"
        }

        fakeData.apply {
            every { results } returns listOf(fakeResult, fakeResult, fakeResult)
        }

        fakeMarvelApiData.apply {
            every { data } returns fakeData
        }

        fakeMarvelRepository.apply {
            coEvery {
                getMarvelComicList(2,0)
            } returns fakeMarvelApiData

            coEvery {
                searchMarvelComic("Spider")
            } returns fakeMarvelApiData
        }

        viewModel = SearchComicsViewModel(fakeMarvelRepository)
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
