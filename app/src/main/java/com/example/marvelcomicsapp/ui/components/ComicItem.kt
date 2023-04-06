package com.example.marvelcomicsapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.marvelcomicsapp.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ComicItem(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    author: String,
    url: String,
    cornerRadius: Dp = 7.dp
) {

    Box(
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(cornerRadius))
            .background(color = Color.White, shape = RoundedCornerShape(cornerRadius))
            .fillMaxWidth()
            .heightIn(0.dp, 200.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.35f)
            ) {
                GlideImage(
                    model = url,
                    contentDescription = stringResource(id = R.string.comics_book_covers),
                    modifier = Modifier.clip(RoundedCornerShape(cornerRadius)),
                    contentScale = ContentScale.Crop
                )
            }
            ComicInfo(
                title = title,
                author = author,
                description = description
            )
        }
    }
}

@Composable
fun ComicInfo(
    title: String,
    author: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(10.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "${stringResource(id = R.string.written_by)} $author",
            style = MaterialTheme.typography.body2,
            color = Color.Gray
        )
        Text(
            modifier = Modifier.padding(0.dp, 10.dp),
            text = description,
            style = MaterialTheme.typography.body1,
            color = Color.Gray
        )
    }
}