package com.example.feature_main.ui.comiclist

import android.app.Activity

sealed class ComicListEvent{
    data class Logout(val activity: Activity?): ComicListEvent()
    data class AddComicsToFavourite(val comicsId: Int): ComicListEvent()
}
