package com.omsk.railwaymuseum.ui.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omsk.railwaymuseum.databinding.CardReviewImagesBinding
import java.io.File

class ReviewAddingAdapter(private val clickListenerImageFull: ClickListenerImageFull,
                          private val clickListenerImageDelete: ClickListenerImageDelete):
    ListAdapter<File, ReviewAddingAdapter.ViewHolder>(DiffCallback){

    companion object DiffCallback : DiffUtil.ItemCallback<File>() {
        override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = getItem(position)
        holder.bind(dataItem, clickListenerImageFull, clickListenerImageDelete)
    }

    class ViewHolder private constructor(private val binding: CardReviewImagesBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardReviewImagesBinding.inflate(layoutInflater)
                return ViewHolder(binding)
            }
        }

        fun bind(dataItem: File, clickListenerImageFull: ClickListenerImageFull, clickListenerImageDelete: ClickListenerImageDelete) {
            binding.imageFile = dataItem
            binding.clickListener = clickListenerImageFull
            binding.clickListenerDelete = clickListenerImageDelete
            binding.executePendingBindings()
        }
    }
}

class ClickListenerImageFull(val clickListener: (imageFile: File) -> Unit) {
    fun onClick(imageFile: File) = clickListener(imageFile)
}

class ClickListenerImageDelete(val clickListener: (imageFile: File) -> Unit) {
    fun onClick(imageFile: File) = clickListener(imageFile)
}