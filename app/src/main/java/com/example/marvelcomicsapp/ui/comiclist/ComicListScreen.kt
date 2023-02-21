package com.example.marvelcomicsapp.ui.comiclist

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.marvelcomicsapp.R
import com.example.marvelcomicsapp.ui.components.ComicsListLazyColumn

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ComicListScreen(
    navController: NavController,
    viewModel: ComicListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        topBar = {
            ComicListHeader()
        },
        scaffoldState = scaffoldState
    ) {
        ComicsListLazyColumn(
            comicBooks = state.comicBooks,
            navController = navController,
            loadItems = { viewModel.loadComicsPaginated() },
            pagination = true
        )
    }
}


@Preview
@Composable
fun ComicListHeader() {
    Box(
        modifier = Modifier
            .shadow(20.dp, RectangleShape)
            .fillMaxWidth()
            .background(White)
            .padding(15.dp, 20.dp, 10.dp, 5.dp)
    ) {
        Text(
            text = stringResource(id = R.string.marvel_comics),
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold
        )
    }
}



