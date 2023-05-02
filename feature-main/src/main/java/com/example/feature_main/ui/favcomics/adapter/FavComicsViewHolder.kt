package com.example.feature_main.ui.favcomics.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.data.remote.firebase.ComicData
import com.example.feature_main.databinding.ComicsItemBinding

class FavComicsViewHolder(val binding: ComicsItemBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(comicData: ComicData){
        Glide.with(binding.ivComicsCover.context)
            .load(comicData.image)
            .centerCrop()
            .into(binding.ivComicsCover)

        binding.tvComicTitle.text = comicData.title
        binding.tvAuthors.text = if(comicData.authors.isBlank()) "" else "Written by" + comicData.authors
        binding.tvDescription.text = comicData.description
    }
}