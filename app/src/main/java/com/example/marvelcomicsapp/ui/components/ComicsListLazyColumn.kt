package com.example.marvelcomicsapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.marvelcomicsapp.R
import com.example.core.data.remote.responses.Result
import com.example.marvelcomicsapp.ui.theme.Red100
import com.example.core.util.Screen

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
                    top = 0.dp,
                    start = 15.dp,
                    end = 15.dp,
                    bottom = 50.dp
                )
        ) {

            itemsIndexed(comicBooks) { index, comics ->

                if(index == 0){
                    Spacer(modifier = Modifier.height(16.dp))
                }


                val imgUrl = if (comics.images.isNotEmpty()) {
                    "${comics.images[0].path}.${comics.images[0].extension}"
                } else {
                    ""
                }

                ComicItem(
                    title = comics.title,
                    description = comics.description ?: "",
                    author = if (comics.creators.items.isEmpty()) {
                        null
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

                if (pagination) {
                    if (index >= comicBooks.count() - 1 && !endReached) {
                        loadItems()
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator( color = Red100)
                        }
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        }
    }
}