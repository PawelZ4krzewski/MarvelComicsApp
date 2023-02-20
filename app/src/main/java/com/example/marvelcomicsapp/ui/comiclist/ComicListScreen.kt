package com.example.marvelcomicsapp.ui.comiclist

import android.util.Log
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.marvelcomicsapp.ui.components.ComicItem

@Composable
fun ComicListScreen(
    navController: NavController,
    viewModel: ComicListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier
            .shadow(10.dp, RectangleShape)
            .fillMaxWidth()
            .background(White)
            .padding(15.dp, 20.dp, 10.dp, 5.dp)
        ){
            Text(
                text = "Marvel Comics",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold
            )
        }

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(
                    15.dp,
                    0.dp
                )
        ){

            itemsIndexed(state.comicBooks) { index, comics ->
                if (index >= state.comicBooks.count() - 1 && !state.endReached) {
                    Log.d(
                        "ComicListScreen",
                        "Pobieram nowe dane. Obecbna ilosc to: ${state.comicBooks.count()} czy koniec ${state.endReached}"
                    )
                    viewModel.loadComicsPaginated()
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Log.d(
                        "ComicListScreen",
                        "Nie pobieram. Obecbna ilosc to: ${state.comicBooks.count()}, a index to $index czy koniec ${state.endReached}"
                    )

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
                    "https://i.pinimg.com/736x/b5/34/df/b534df05c4b06ebd32159b2f9325d83f.jpg"
                }

                val creators = if (comics.creators.items.isEmpty()) {
                    "Unknown"
                }else{
                    comics.creators.items.joinToString { creator -> creator.name }
                }

                ComicItem(comics.title, description, creators, imgUrl)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}



