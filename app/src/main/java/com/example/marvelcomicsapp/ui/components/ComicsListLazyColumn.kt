package com.example.marvelcomicsapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.marvelcomicsapp.R
import com.example.marvelcomicsapp.data.remote.responses.Result
import com.example.marvelcomicsapp.util.Screen

@Composable
fun ComicsListLazyColumn(
    comicBooks: List<Result>,
    navController: NavController,
    loadItems: () -> Unit,
    pagination: Boolean = false,
    endReached: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(
                    15.dp,
                    0.dp
                )
        ) {

            itemsIndexed(comicBooks) { index, comics ->

                if (pagination) {
                    if (index >= comicBooks.count() - 1 && !endReached) {
                        loadItems()
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }


                val description = if (comics.description.isNullOrBlank()) {
                    ""
                } else {

                    if (comics.description.count() >= 100) {
                        comics.description.take(100) + "..."
                    } else {
                        comics.description
                    }

                }

                val imgUrl = if (comics.images.isNotEmpty()) {
                    "${comics.images[0].path}.${comics.images[0].extension}"
                } else {
                    stringResource(id = R.string.comic_cover_placeholder)
                }

                ComicItem(
                    title = comics.title,
                    description = description,
                    author = if (comics.creators.items.isEmpty()) {
                        stringResource(id = R.string.unknown)
                    } else {
                        comics.creators.items.joinToString { creator -> creator.name }
                    },
                    url = imgUrl,
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Screen.ComicsDetailsScreen.route + "?comicsBook=${comics.id}")
                        }

                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}