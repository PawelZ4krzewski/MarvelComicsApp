package com.example.marvelcomicsapp.ui.comicsdetails

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.marvelcomicsapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ComicsDetailsScreen(
    navController: NavController,
    viewModel: ComicsDetailsViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    val scope = rememberCoroutineScope()

    val scroll = rememberScrollState(0)


    BottomSheetScaffold(
        sheetShape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp
        ),
        sheetContent = {
            state.comicBook.let {
                if (it != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(600.dp)
                            .background(Color.Transparent)
                            .padding(10.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        PeekScaffoldButton(
                            scope = scope,
                            sheetState = sheetState
                        )

                        BottomSheet(
                            title = it.title,
                            creators = if (it.creators.items.isEmpty()) {
                                stringResource(id = R.string.unknown)
                            } else {
                                it.creators.items.joinToString { creator -> creator.name }
                            },
                            description = if (it.description.isNullOrBlank()) {
                                ""
                            } else {
                                it.description
                            },
                            scroll = scroll,
                            url = it.urls[0].url,
                            modifier = Modifier
                                .height(600.dp)
                                .padding(10.dp)
                        )
                    }
                }
            }

        },
        sheetBackgroundColor = Color.White,
        sheetPeekHeight = 200.dp,
        scaffoldState = bottomSheetScaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                }
                Box(
                    Modifier.weight(1f),
                    contentAlignment = Alignment.Center

                )
                {
                    Text(
                        text = stringResource(id = R.string.details),
                        style = MaterialTheme.typography.h5
                    )
                }
                Box(Modifier.weight(1f))
            }
            if (state.comicBook != null) {
                BackgroundImage(
                    url = "${state.comicBook.thumbnail.path}.${state.comicBook.thumbnail.extension}",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun BottomSheet(
    title: String,
    creators: String,
    description: String,
    scroll: ScrollState,
    url: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .fillMaxWidth()
            .verticalScroll(scroll),
        verticalArrangement = SpaceBetween
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(0.dp, 7.dp),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = creators,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 5.dp)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.h6,
            )
        }
        MoreInfoButton(url)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PeekScaffoldButton(
    scope: CoroutineScope,
    sheetState: BottomSheetState
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier
            .fillMaxWidth(0.20f)
            .height(7.5.dp)
            .background(Color.LightGray, RoundedCornerShape(10.dp))
            .clickable {
                scope.launch {
                    if (sheetState.isCollapsed) {
                        sheetState.expand()
                    } else {
                        sheetState.collapse()
                    }
                }
            }
        )
    }
}

@Composable
fun MoreInfoButton(
    url: String
) {
    val uriHandler = LocalUriHandler.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(bottom = 50.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(
            onClick = {
                uriHandler.openUri(url)
            },
            modifier = Modifier
                .height(50.dp)
                .width(250.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.find_out_more),
                style = MaterialTheme.typography.body1,
                color = Color.White
            )
        }
    }
}

@Composable
fun BackgroundImage(
    url: String,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = stringResource(id = R.string.comics_book_covers),
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