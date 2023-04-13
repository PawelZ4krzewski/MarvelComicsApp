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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.marvelcomicsapp.R
import com.example.marvelcomicsapp.ui.components.HtmlText
import com.example.marvelcomicsapp.ui.theme.ComicTitle
import com.example.marvelcomicsapp.ui.theme.Red100
import com.example.marvelcomicsapp.ui.theme.Gray400
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

    Scaffold(
        floatingActionButton = {
            MoreInfoButton(
                state.comicBook?.urls?.get(0)?.url ?: ""
            )
        },
    ) {
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
                                    null
                                } else {
                                    it.creators.items.joinToString { creator -> creator.name }
                                },
                                description = if (it.description.isNullOrBlank()) {
                                    ""
                                } else {
                                    it.description
                                },
                                scroll = scroll,
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
            scaffoldState = bottomSheetScaffoldState,

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(15.dp, 20.dp, 10.dp, 20.dp),
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
                            style = MaterialTheme.typography.h5,
                            fontWeight = FontWeight.Bold
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
}

@Composable
fun BottomSheet(
    title: String,
    creators: String?,
    description: String,
    scroll: ScrollState,
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
        Column( modifier = Modifier.padding(bottom = 125.dp)){
            Text(
                text = title,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(0.dp, 7.dp),
            )
            if(!creators.isNullOrBlank()){
                Text(
                    text = creators,
                    style = MaterialTheme.typography.body1,
                    color = Gray400,
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 5.dp)
                )
            }
            HtmlText(
                html = description,
                style = MaterialTheme.typography.h6
            )
        }
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
            .offset(x = 16.dp)
            .advancedShadow(
                shadowBlurRadius = 25.dp,
                offsetY = (-20).dp,
                color = Color.White
            )
            .background(Color.White)
            .padding(bottom = 50.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(
            onClick = {
                uriHandler.openUri(url)
            },
            modifier = Modifier
                .height(50.dp)
                .width(300.dp)
                ,
            colors = ButtonDefaults.buttonColors(backgroundColor = Red100),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.find_out_more),
                style = MaterialTheme.typography.body1,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BackgroundImage(
    url: String,
    modifier: Modifier = Modifier
) {

    GlideImage(
        model = url,
        contentDescription = stringResource(id = R.string.comics_book_covers),
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

fun Modifier.advancedShadow(
    color: Color = Color.Black,
    alpha: Float = 1f,
    cornersRadius: Dp = 0.dp,
    shadowBlurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = drawBehind {

    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparentColor = color.copy(alpha = 0f).toArgb()

    drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        frameworkPaint.setShadowLayer(
            shadowBlurRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            shadowColor
        )
        it.drawRoundRect(
            0f,
            0f,
            this.size.width,
            this.size.height,
            cornersRadius.toPx(),
            cornersRadius.toPx(),
            paint
        )
    }
}