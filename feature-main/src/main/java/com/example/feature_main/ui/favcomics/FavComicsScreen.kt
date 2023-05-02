package com.example.feature_main.ui.favcomics

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.feature_main.databinding.FavComicsFragmentLayoutBinding
import com.example.feature_main.ui.favcomics.adapter.FavComicsAdapter

@Composable
fun FavComicsScreen(
    navController: NavController,
    viewModel: FavComicsViewModel = hiltViewModel()
){
    val state = viewModel.state.value
    val localContext = LocalContext.current
    AndroidViewBinding(FavComicsFragmentLayoutBinding::inflate){
        val adapter = FavComicsAdapter(state.favComicsBooks, navController)

        this.rvFavComics.apply{
            this.layoutManager = LinearLayoutManager(localContext)
            this.adapter = adapter
        }
    }


}

