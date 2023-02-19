package com.example.marvelcomicsapp.ui.comiclist

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun ComicListScreen(
    navController: NavController,
    viewModel: ComicListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    val testComic = TestComics("Stan Lee", "Amazing Spider-man", "sdsadsad asd sa dsad as das dasdsad asd asdd sad asd sd sa dsad as das d da sd asd as dasd")
    val testComicsList = mutableListOf<TestComics>()
    for(i in 0..10)
        testComicsList.add(testComic)

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

            itemsIndexed(state.comicBooks){index, comics ->
//                if(index >= testComicsList.count() - 1 && !state.endReached)
//                {
//
//                }

                val description = if (comics.description.isNullOrBlank()){
                    ""
                }
                else{
                    comics.description
                }

                val creators = comics.creators.items.map { it.name }.joinToString()

                ComicItem(comics.title, description, creators)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

}

@Preview
@Composable
fun ComicScreen (){

    val testComic = TestComics("Stan Lee", "Amazing Spider-man", "sdsadsad asd sa dsad as das dasdsad asd asdd sad asd sd sa dsad as das d da sd asd as dasd")
    val testComicsList = mutableListOf<TestComics>()
    for(i in 0..10)
        testComicsList.add(testComic)

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

            items(testComicsList){testComicItem: TestComics ->
//                if(testComicItem >= testComicsList.count() - 1 && state.value)
                ComicItem(testComicItem.title, testComicItem.description, testComicItem.author)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


@Composable
fun ComicItem(
    title: String,
    description: String,
    author: String,
    cornerRadius: Dp = 7.dp
){

    Box(
        modifier = Modifier
            .shadow(2.dp, RoundedCornerShape(cornerRadius))
            .background(color = White, shape = RoundedCornerShape(cornerRadius))
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
    ){
        Row(modifier = Modifier
            .fillMaxSize()
        ) {
            Box(modifier = Modifier
                .background(Red)
                .fillMaxHeight()
                .fillMaxWidth(0.3f)
            )
            Column( modifier = Modifier
                .fillMaxHeight()
                .padding(10.dp)
            )
            {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "written by $author",
                    style = MaterialTheme.typography.body2,
                    color = Gray
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.body1,
                    color = Gray
                )
            }
        }
    }

}
class TestComics(val author: String, val title: String, val description: String)