package com.example.mdc

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mdc.databinding.ImageItemBinding


class ImageViewHolder(
    private val binding: ImageItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: String) {
        with(binding) {
            Glide.with(this.root).load(item)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView)
        }
    }
}