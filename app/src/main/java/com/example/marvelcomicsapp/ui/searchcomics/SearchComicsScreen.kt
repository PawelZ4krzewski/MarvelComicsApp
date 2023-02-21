package com.example.marvelcomicsapp.ui.searchcomics

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.marvelcomicsapp.R
import com.example.marvelcomicsapp.ui.components.ComicsListLazyColumn

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchComicsScreen(
    navController: NavController, viewModel: SearchComicsViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        topBar = {
            SearchComicsHeader(
                state = state, focusRequester = focusRequester, focusManager = focusManager
            )
        }, scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            if (!state.isSearched) {
                GreetingSearchColumn()
            } else {
                if (state.isFoundComics) {

                    ComicsListLazyColumn(comicBooks = state.comicBooks,
                        navController = navController,
                        loadItems = { viewModel.searchComicsBook(state.searchComicText) }
                    )

                } else {
                    AnyBooksColumn()
                }
            }
        }
    }
}

@Composable
fun SearchComicsHeader(
    state: SearchComicsState,
    focusRequester: FocusRequester,
    focusManager: FocusManager,
    viewModel: SearchComicsViewModel = hiltViewModel()
) {
    Row(
        modifier = Modifier
            .padding(20.dp, 20.dp)
            .fillMaxWidth()
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        TransparentHintTextField(
            text = state.searchComicText,
            hint = state.searchComicHint,
            onValueChange = {
                viewModel.onEvent(SearchComicsEvent.EnterText(it))
            },
            onFocusChange = {
                viewModel.onEvent(SearchComicsEvent.ChangeText(it))
            },
            focusRequester = focusRequester,
            searchComics = {
                viewModel.onEvent(SearchComicsEvent.SearchComics)
            },
            isHintVisible = state.isSearchComicHintVisible,
            singleLine = true,
            modifier = Modifier
                .background(Color.LightGray, RoundedCornerShape(7.dp))
                .padding(5.dp)
        )
        if (!state.isSearchComicHintVisible) {
            TextButton(onClick = {
                viewModel.onEvent(SearchComicsEvent.EnterText(""))
                focusManager.clearFocus()
            }) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = MaterialTheme.typography.h6,
                    color = Color.LightGray
                )
            }
        }
    }
}

@Preview
@Composable
fun GreetingSearchColumn() {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.MenuBook,
            contentDescription = stringResource(id = R.string.book),
            tint = Color.LightGray,
            modifier = Modifier.fillMaxSize(0.3f)
        )

        Text(
            text = stringResource(id = R.string.start_typing_to_find),
            style = MaterialTheme.typography.h6
        )
    }
}

@Preview
@Composable
fun AnyBooksColumn() {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.SentimentDissatisfied,
            contentDescription = stringResource(id = R.string.sad_face),
            tint = Color.LightGray,
            modifier = Modifier.fillMaxSize(0.3f)
        )

        Text(
            text = stringResource(id = R.string.cant_find_comics),
            style = MaterialTheme.typography.h6
        )
    }
}

@Composable
fun TransparentHintTextField(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    isHintVisible: Boolean = true,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = false,
    onFocusChange: (FocusState) -> Unit,
    focusRequester: FocusRequester,
    searchComics: () -> Unit
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        val searchColor = {
            if (isHintVisible) {
                Color.Gray
            } else {
                Color.Black
            }
        }
        IconButton(onClick = {
            searchComics()
        }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = R.string.search_icon),
                tint = searchColor.invoke()
            )
        }
        Box {
            BasicTextField(value = text,
                onValueChange = onValueChange,
                singleLine = singleLine,
                textStyle = MaterialTheme.typography.h6,
                modifier = Modifier
                    .fillMaxWidth(
                        if (isHintVisible) {
                            1f
                        } else 0.7f
                    )
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        onFocusChange(it)
                    })
            if (isHintVisible) {
                Text(
                    text = hint, style = MaterialTheme.typography.h6, color = Color.Gray
                )
            }
        }
    }
}