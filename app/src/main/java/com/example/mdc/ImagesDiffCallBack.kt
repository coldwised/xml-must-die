package com.example.mdc

import androidx.recyclerview.widget.DiffUtil

object ImagesDiffCallBack : DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(oldItem: String, newItem: String) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String) =
        oldItem == newItem
}