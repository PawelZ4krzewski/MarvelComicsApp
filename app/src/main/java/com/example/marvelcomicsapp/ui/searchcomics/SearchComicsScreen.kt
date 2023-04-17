package com.example.marvelcomicsapp.ui.searchcomics

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.marvelcomicsapp.R
import com.example.marvelcomicsapp.ui.components.ComicsListLazyColumn
import com.example.marvelcomicsapp.ui.theme.Gray300
import com.example.marvelcomicsapp.ui.theme.Gray500
import com.example.marvelcomicsapp.ui.theme.LightGray100

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchComicsScreen(
    navController: NavController, viewModel: SearchComicsViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val scaffoldState = rememberScaffoldState()

    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        state = topAppBarState
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SearchComicsHeader(
                state = state, focusRequester = focusRequester, focusManager = focusManager, scrollBehavior = scrollBehavior
            )
        }, scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchComicsHeader(
    state: SearchComicsState,
    focusRequester: FocusRequester,
    focusManager: FocusManager,
    scrollBehavior: TopAppBarScrollBehavior,
    viewModel: SearchComicsViewModel = hiltViewModel()
) {
    Row(
        modifier = if (scrollBehavior.state.contentOffset < -10f) {
            Modifier.shadow(elevation = 20.dp, shape = RectangleShape)
                .background(Color.White)
                .fillMaxWidth()
                .padding(20.dp, 20.dp)
                .padding(5.dp)
        } else Modifier
            .padding(20.dp, 20.dp)
            .fillMaxWidth()
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        TransparentHintTextField(
            text = state.searchComicText,
            hint = state.searchComicHint,
            onValueChange = {
                viewModel.apply {
                    onEvent(SearchComicsEvent.EnterText(it))
                    onEvent(SearchComicsEvent.SearchComics)
                    onEvent(SearchComicsEvent.ChangeText)
                }
            },
            onFocusChange = {},
            focusRequester = focusRequester,
            searchComics = {
                viewModel.onEvent(SearchComicsEvent.SearchComics)
            },
            isHintVisible = state.isSearchComicHintVisible,
            singleLine = true,
            modifier = Modifier
                .background(LightGray100, RoundedCornerShape(7.dp))
                .padding(5.dp)
        )
        if (!state.isSearchComicHintVisible) {
            TextButton(onClick = {
                viewModel.apply{
                    onEvent(SearchComicsEvent.EnterText(""))
                    onEvent(SearchComicsEvent.CancelSearching)
                }
                focusManager.clearFocus()
            }) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = MaterialTheme.typography.h6,
                    color = Gray300
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
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.vector_book),
            contentDescription = stringResource(id = R.string.book),
            tint = Color.LightGray,
            modifier = Modifier.fillMaxSize(0.3f)
        )

        Text(
            text = stringResource(id = R.string.start_typing_to_find),
            style = MaterialTheme.typography.h6
        )
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Preview
@Composable
fun AnyBooksColumn() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.anybook),
            contentDescription = stringResource(id = R.string.sad_face),
            tint = Color.LightGray,
            modifier = Modifier.fillMaxSize(0.3f)
        )

        Text(
            text = stringResource(id = R.string.cant_find_comics),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@OptIn(ExperimentalComposeUiApi::class)
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
    searchComics: () -> Unit,
    imeAction: ImeAction = ImeAction.Done,
) {
    val localKeyboardController = LocalSoftwareKeyboardController.current
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        val searchColor = { if (isHintVisible) Gray300 else Gray500 }
        IconButton(onClick = {
            searchComics()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.search_icon),
                contentDescription = stringResource(id = R.string.search_icon),
                tint = searchColor.invoke()
            )
        }
        Box {
            TextField(
                value = text,
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
                    }
                    .align(Alignment.Center),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = imeAction
                ),
                keyboardActions = KeyboardActions {
                    searchComics()
                    localKeyboardController?.hide()
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = searchColor.invoke(),
                    backgroundColor = Color.Transparent,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        text = hint, style = MaterialTheme.typography.h6, color = searchColor.invoke(), maxLines = 1
                    )
                }
            )
        }
    }
}