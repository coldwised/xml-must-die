package com.example.mdc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.mdc.databinding.ImageItemBinding

class ImageListAdapter(
    private val onItemClicked: (String) -> Unit
) : ListAdapter<String, ImageViewHolder>(ImagesDiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ImageViewHolder(
            ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClicked
        )

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) =
        holder.bind(getItem(position))
}