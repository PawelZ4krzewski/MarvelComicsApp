package com.example.marvelcomicsapp.ui.searchcomics

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.marvelcomicsapp.ui.components.ComicItem
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.format.TextStyle

@Composable
fun SearchComicsScreen(
    navController: NavController,
    viewModel: SearchComicsViewModel = hiltViewModel()
){
    val state = viewModel.state.value

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(modifier = Modifier
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
            if(!state.isSearchComicHintVisible){
                TextButton(onClick = {
                    viewModel.onEvent(SearchComicsEvent.EnterText(""))
                    focusManager.clearFocus()
                }) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.h6,
                        color = Color.LightGray
                    )
                }
            }
        }
        if(!state.isSearched){
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.7f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = "Book",
                    tint = Color.LightGray,
                    modifier = Modifier.fillMaxSize(0.3f)
                )

                Text(
                    text = "Start typing to find a particular comics",
                    style = MaterialTheme.typography.h6
                )
            }
        } else{
            if (state.isFoundComics) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            15.dp,
                            0.dp
                        )
                ) {
                    items(state.comicBooks) {
                        val description = if (it.description.isNullOrBlank()) {
                            ""
                        } else {

                            if (it.description.count() >= 100) {
                                it.description.take(100) + "..."
                            } else {
                                it.description
                            }

                        }

                        val imgUrl = if (it.images.isNotEmpty()) {
                            "${it.images[0].path}.${it.images[0].extension}"
                        } else {
                            "https://i.pinimg.com/736x/b5/34/df/b534df05c4b06ebd32159b2f9325d83f.jpg"
                        }

                        val creators = if (it.creators.items.isEmpty()) {
                            "Unknown"
                        } else {
                            it.creators.items.joinToString { creator -> creator.name }
                        }

                        ComicItem(it.title, description, creators, imgUrl)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(0.7f)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SentimentDissatisfied,
                        contentDescription = "Book",
                        tint = Color.LightGray,
                        modifier = Modifier.fillMaxSize(0.3f)
                    )

                    Text(
                        text = "Can't find comics with this title",
                        style = MaterialTheme.typography.h6
                    )
                }
            }
        }

    }
}

@Composable
fun TransparentHintTextField(
    text: String,
    hint: String,
    modifier: Modifier =  Modifier,
    isHintVisible: Boolean = true,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = false,
    onFocusChange: (FocusState) -> Unit,
    focusRequester: FocusRequester,
    searchComics: () -> Unit
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val searchColor = {
            if(isHintVisible){
                Color.Gray
            }else{
                Color.Black
            }
        }
        IconButton(onClick = {
            searchComics()
        }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = searchColor.invoke()
            )
        }

        Box{
            BasicTextField(
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
            )
            if(isHintVisible){
                Text(
                    text = hint,
                    style = MaterialTheme.typography.h6,
                    color = Color.Gray
                )
            }
        }


    }

}