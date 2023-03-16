package com.example.mdc

import android.view.View.MeasureSpec
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.mdc.databinding.ImageItemBinding


class ImageViewHolder(
    private val binding: ImageItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: String) {
        with(binding) {
            root.load(item)
            {
                //scale(Scale.FILL)
                crossfade(true)
                placeholder(R.drawable.ic_launcher_foreground)
                //transformations(CircleCropTransformation())
            }
        }
    }
}