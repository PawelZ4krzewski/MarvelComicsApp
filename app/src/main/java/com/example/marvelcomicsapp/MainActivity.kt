package com.example.marvelcomicsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.Glide
import com.example.marvelcomicsapp.ui.comiclist.ComicListScreen
import com.example.marvelcomicsapp.ui.comiclist.ComicListViewModel
import com.example.marvelcomicsapp.ui.theme.MarvelComicsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarvelComicsAppTheme {
//                Greeting(name = "DUPA")
                val navControler = rememberNavController()
                NavHost(
                    navController = navControler,
                    startDestination = "comic_list_screen"
                ){
                    composable("comic_list_screen"){
                        ComicListScreen(navController = navControler)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MarvelComicsAppTheme {
        Greeting("Android")
    }
}