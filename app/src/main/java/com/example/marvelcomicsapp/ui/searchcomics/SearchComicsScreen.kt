package com.example.marvelcomicsapp.ui.searchcomics

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
                TextButton(onClick = { focusManager.clearFocus() }) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.h6,
                        color = Color.LightGray
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
                    .fillMaxWidth( if(isHintVisible) {1f} else 0.7f )
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