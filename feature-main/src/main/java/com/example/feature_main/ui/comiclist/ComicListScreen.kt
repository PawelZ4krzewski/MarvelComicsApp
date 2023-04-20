package com.example.feature_main.ui.comiclist

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.feature_main.R
import com.example.feature_main.ui.components.ComicsListLazyColumn
import com.example.feature_main.ui.theme.HeaderComicList
import com.example.feature_main.ui.theme.Red100

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ComicListScreen(
    navController: NavController,
    viewModel: ComicListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()

    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        state = topAppBarState
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ComicListHeader(scrollBehavior = scrollBehavior)
        },
        scaffoldState = scaffoldState
    ) {
        if (state.comicBooks.isEmpty()){
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator( color = Red100)
            }
        } else{
            ComicsListLazyColumn(
                comicBooks = state.comicBooks,
                navController = navController,
                loadItems = { viewModel.loadComicsPaginated() },
                pagination = true
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComicListHeader(
    scrollBehavior: TopAppBarScrollBehavior
) {
    Box(
        modifier = if (scrollBehavior.state.contentOffset < -10f) {
            Modifier.shadow(elevation = 20.dp, shape = RectangleShape)
                .fillMaxWidth()
                .background(White)
                .padding(15.dp, 20.dp, 10.dp, 5.dp)
        } else Modifier
            .fillMaxWidth()
            .background(White)
            .padding(15.dp, 20.dp, 10.dp, 5.dp)
    ) {
        Text(
            text = stringResource(id = R.string.marvel_comics),
            style = MaterialTheme.typography.HeaderComicList,
        )
    }
}



