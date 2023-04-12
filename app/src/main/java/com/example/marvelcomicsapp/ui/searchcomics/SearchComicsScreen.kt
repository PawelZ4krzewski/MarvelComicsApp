package com.example.marvelcomicsapp.ui.searchcomics

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.marvelcomicsapp.ui.theme.Gray400
import com.example.marvelcomicsapp.ui.theme.LightGray100

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
                viewModel.apply {
                    onEvent(SearchComicsEvent.EnterText(it))
                    onEvent(SearchComicsEvent.SearchComics)
                }
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
            .fillMaxHeight()
            .fillMaxWidth(),
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
        Spacer(modifier = Modifier.height(175.dp))
    }
}

@Preview
@Composable
fun AnyBooksColumn() {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
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
        Spacer(modifier = Modifier.height(200.dp))
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
        val searchColor = { if (isHintVisible) Gray300 else Gray400 }
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