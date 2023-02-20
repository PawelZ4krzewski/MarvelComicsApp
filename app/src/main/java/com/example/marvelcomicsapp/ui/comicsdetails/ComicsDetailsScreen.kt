package com.example.marvelcomicsapp.ui.comicsdetails

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ComicsDetailsScreen(
    navController: NavController,
    viewModel: ComicsDetailsViewModel = hiltViewModel()
){
    val state = viewModel.state.value

    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed

    )
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    val scope = rememberCoroutineScope()


    BottomSheetScaffold(
        sheetContent = {
                       Box(modifier = Modifier
                           .fillMaxWidth()
                           .height(300.dp)
                       ) {
                           Button(onClick = {
                               scope.launch {
                                   if(sheetState.isCollapsed){
                                       sheetState.expand()
                                   } else{
                                       sheetState.collapse()
                                   }
                               }
                           }) {
                               
                           }
                            Text(text = "DUPA")
                       }
        },
        sheetBackgroundColor = Color.Red,
        scaffoldState = bottomSheetScaffoldState
    ) {
        Column(

            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(15.dp, 10.dp, 10.dp, 5.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .weight(1f),
                    contentAlignment = Alignment.CenterStart
                )
                {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
                Box(
                    Modifier.weight(1f),
                    contentAlignment = Alignment.Center

                )
                {
                    Text(
                        text = "Details",
                        style = MaterialTheme.typography.h5
                    )
                }
                Box(Modifier.weight(1f))
            }
            if (state.comicBook != null) {
                val imgUrl = if (state.comicBook.images.isNotEmpty()) {
                    "${state.comicBook.images[0].path}.${state.comicBook.images[0].extension}"
                } else {
                    "https://i.pinimg.com/736x/b5/34/df/b534df05c4b06ebd32159b2f9325d83f.jpg"
                }
                BackgroundImage(
                    url = imgUrl,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun BackgroundImage(
    url: String,
    modifier: Modifier = Modifier
){
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = "Comics Book covers",
        contentScale = ContentScale.Crop,
        loading = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.scale(0.5f)
                )
            }
        },
        modifier = modifier
    )
}