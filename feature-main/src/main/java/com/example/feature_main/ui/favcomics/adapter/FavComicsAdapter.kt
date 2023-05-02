package com.example.feature_main.ui.favcomics.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.core.data.remote.firebase.ComicData
import com.example.core.util.Screen
import com.example.feature_main.databinding.ComicsItemBinding
import com.example.feature_main.ui.favcomics.FavComicsEvent
import com.example.feature_main.ui.favcomics.FavComicsViewModel

class FavComicsAdapter(
    private val comics: List<ComicData>,
    private val navController: NavController,
    private val viewModel: FavComicsViewModel
): RecyclerView.Adapter<FavComicsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavComicsViewHolder {
        val favComicsBinding = ComicsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavComicsViewHolder(favComicsBinding)
    }

    override fun getItemCount() = comics.size

    override fun onBindViewHolder(holder: FavComicsViewHolder, position: Int) {

        holder.bind(comics[position])

        holder.binding.llComicItem.setOnClickListener{
            navController.navigate(Screen.ComicsDetailsScreen.route + "?comicsBook=${comics[position].comicId}")
        }

        holder.binding.ibtUnlike.setOnClickListener {
            viewModel.onEvent(FavComicsEvent.AddComicsToFavourite(comics[position]))
        }
    }

}
