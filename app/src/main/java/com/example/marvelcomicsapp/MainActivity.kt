package com.example.marvelcomicsapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bumptech.glide.Glide
import com.example.marvelcomicsapp.ui.comiclist.ComicListScreen
import com.example.marvelcomicsapp.ui.comicsdetails.ComicsDetailsScreen
import com.example.marvelcomicsapp.ui.searchcomics.SearchComicsScreen
import com.example.marvelcomicsapp.ui.theme.MarvelComicsAppTheme
import com.example.marvelcomicsapp.util.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarvelComicsAppTheme {
//                Greeting(name = "DUPA")
                val navControler = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(
                            items = listOf(
                                BottomNavItem(
                                    name = "Comics list",
                                    route = "comic_list_screen",
                                    icon = Icons.Default.Home
                                ),
                                BottomNavItem(
                                    name = "Comics search",
                                    route = "search_comics_screen",
                                    icon = Icons.Default.Search
                                )
                            ),
                            navController = navControler,
                            onItemClick = {
                                navControler.navigate(it.route)
                            }
                        )
                    }
                ){
                    Navigation(navController = navControler)
                }
//                NavHost(
//                    navController = navControler,
//                    startDestination = "comic_list_screen"
//                ) {
//                    composable("comic_list_screen") {
//                        ComicListScreen(navController = navControler)
//                    }
//                }
            }
        }
    }
}

@Composable
fun Navigation(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Screen.ComicListScreen.route) {
        composable(Screen.ComicListScreen.route) {
            ComicListScreen(navController = navController)
        }
        composable(Screen.SearchComicListScreen.route) {
            SearchComicsScreen(navController = navController)
        }

        composable(
            route = Screen.ComicsDetailsScreen.route + "?comicsBook={comicsBook}",
            arguments = listOf(
                navArgument(
                    name = "comicsBook"
                ){
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ){
            ComicsDetailsScreen(navController = navController)
        }
    }

}

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(
        modifier = modifier,
        backgroundColor = androidx.compose.ui.graphics.Color.White,
        elevation = 5.dp

    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(
                selected = selected,
                onClick = { onItemClick(item) },
                selectedContentColor = androidx.compose.ui.graphics.Color.Red,
                unselectedContentColor = androidx.compose.ui.graphics.Color.LightGray,
                icon = {
                    Column(horizontalAlignment = CenterHorizontally) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.name
                        )
                    }
                }

            )
        }
    }
}

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector
)