package com.example.feature_main.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.core.util.Screen
import com.example.feature_main.R
import com.example.feature_main.ui.comiclist.ComicListScreen
import com.example.feature_main.ui.searchcomics.SearchComicsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            com.example.feature_main.ui.theme.MarvelComicsAppTheme {
                val navController = rememberNavController()
                WindowCompat.setDecorFitsSystemWindows(window, false)

                val isKeyboardOpen by keyboardAsState()

                Scaffold(
                    modifier = Modifier.statusBarsPadding().systemBarsPadding(),
                    bottomBar = {
                        if (!isKeyboardOpen) {
                            BottomNavigationBar(
                                items = listOf(
                                    BottomNavItem(
                                        name = stringResource(id = R.string.comic_list),
                                        route = Screen.ComicListScreen.route,
                                        icon = painterResource(id = R.drawable.home_icon),
                                    ),
                                    BottomNavItem(
                                        name = stringResource(id = R.string.comics_search),
                                        route = Screen.SearchComicListScreen.route,
                                        icon = painterResource(id = R.drawable.search_icon),
                                    )
                                ),
                                navController = navController,
                                onItemClick = {
                                    navController.navigate(it.route)
                                }
                            )
                        }
                    }
                ) {
                    Navigation(navController = navController)
                }
            }
        }
    }

}

@Composable
fun Navigation(
    navController: NavHostController,
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
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            com.example.feature_main.ui.comicsdetails.ComicsDetailsScreen(navController = navController)
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
                selectedContentColor = com.example.feature_main.ui.theme.Red100,
                unselectedContentColor = androidx.compose.ui.graphics.Color.LightGray,
                icon = {
                    Column(horizontalAlignment = CenterHorizontally) {
                        Icon(
                            modifier = Modifier.size(25.dp) ,
                            painter = item.icon,
                            contentDescription = item.name
                        )
                    }
                },

            )
        }
    }
}

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: Painter
)

@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}