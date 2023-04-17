package com.example.marvelcomicsapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.marvelcomicsapp.ui.comiclist.ComicListScreen
import com.example.marvelcomicsapp.ui.comicsdetails.ComicsDetailsScreen
import com.example.marvelcomicsapp.ui.searchcomics.SearchComicsScreen
import com.example.marvelcomicsapp.ui.theme.MarvelComicsAppTheme
import com.example.marvelcomicsapp.ui.theme.Red100
import com.example.marvelcomicsapp.util.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            MarvelComicsAppTheme {
                val navController = rememberNavController()
                WindowCompat.setDecorFitsSystemWindows(window,false)

                val isKeyboardOpen by keyboardAsState()

                Scaffold(
                    modifier = Modifier.statusBarsPadding().systemBarsPadding(),
                    bottomBar = {
                        if(!isKeyboardOpen) {
                            BottomNavigationBar(
                                items = listOf(
                                    BottomNavItem(
                                        name = stringResource(id = R.string.comic_list),
                                        route = Screen.ComicListScreen.route,
                                        icon = Icons.Default.Home
                                    ),
                                    BottomNavItem(
                                        name = stringResource(id = R.string.comics_search),
                                        route = Screen.SearchComicListScreen.route,
                                        icon = Icons.Default.Search
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
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
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
                selectedContentColor = Red100,
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

@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}