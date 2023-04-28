package com.example.feature_main.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.core.data.remote.responses.FavResult
import com.example.core.util.Screen
import com.example.feature_main.ui.theme.Red100

@Composable
fun ComicsListLazyColumn(
    comicBooks: List<FavResult>,
    navController: NavController,
    loadItems: () -> Unit,
    addToFavourite: (comicId: Int) -> Unit,
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

                var favourite by remember {
                    mutableStateOf(comics.isFavourite)
                }

                if(index == 0){
                    Spacer(modifier = Modifier.height(16.dp))
                }


                var imgUrl = ""
                if (!comics.result.images.isNullOrEmpty()) {
                    comics.result.images?.getOrNull(0)?.let {
                        imgUrl = "${it.path}.${it.extension}"
                    }
                }

                ComicItem(
                    comicId = comics.result.id,
                    title = comics.result.title,
                    description = comics.result.description ?: "",
                    author = if (comics.result.creators.items.isEmpty()) {
                        null
                    } else {
                        comics.result.creators.items.joinToString { creator -> creator.name }
                    },
                    url = imgUrl,
                    isFavourite = favourite,
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Screen.ComicsDetailsScreen.route + "?comicsBook=${comics.result.id}")
                        },
                    addToFavourite = {
                        addToFavourite(it)
                        favourite = !favourite
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