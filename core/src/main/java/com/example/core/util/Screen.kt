package com.example.core.util

sealed class Screen(val route: String) {
    object SearchComicListScreen : Screen("search_comics_screen")
    object ComicListScreen : Screen("comic_list_screen")
    object ComicsDetailsScreen : Screen("comics_details")
    object FavComicsScreen : Screen("fav_comics_details")
}
