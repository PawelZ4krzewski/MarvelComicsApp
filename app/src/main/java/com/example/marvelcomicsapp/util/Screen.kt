package com.example.marvelcomicsapp.util

sealed class Screen(val route: String) {
    object SearchComicListScreen : Screen("search_comics_screen")
    object ComicListScreen : Screen("comic_list_screen")
    object ComicsDetailsScreen : Screen("comics_details")
}
