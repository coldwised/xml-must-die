package com.example.mdc

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mdc.databinding.ImageItemBinding


class ImageViewHolder(
    private val binding: ImageItemBinding,
    private val onItemClicked: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var url: String = ""

    init {
        itemView.setOnClickListener {
            onItemClicked(url)
        }
    }

    fun bind(item: String) {
        with(binding) {
            url = item
            Glide.with(this.root).load(item)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView)
        }
    }
}