package com.example.feature_main.ui.favcomics.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.data.remote.firebase.ComicData
import com.example.core.util.Screen
import com.example.feature_main.databinding.ComicsItemBinding
import com.example.feature_main.ui.favcomics.adapter.FavComicsAdapter.MyViewHolder

class FavComicsAdapter(
    private val comics: List<ComicData>,
    private val navController: NavController
): RecyclerView.Adapter<MyViewHolder>() {

    inner class MyViewHolder(val binding: ComicsItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val favComicsBinding = ComicsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(favComicsBinding)
    }

    override fun getItemCount() = comics.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.llComicItem.setOnClickListener {
            navController.navigate(Screen.ComicsDetailsScreen.route + "?comicsBook=${comics[position].comicId}")
        }

        Glide.with(holder.binding.ivComicsCover.context)
            .load(comics[position].image)
            .centerCrop()
            .into(holder.binding.ivComicsCover)

        holder.binding.tvComicTitle.text = comics[position].title
        holder.binding.tvAuthors.text = if(comics[position].authors.isBlank()) "" else "Written by" + comics[position].authors
        holder.binding.tvDescription.text = comics[position].description
    }


}