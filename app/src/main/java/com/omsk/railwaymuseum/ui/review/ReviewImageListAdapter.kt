package com.omsk.railwaymuseum.ui.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omsk.railwaymuseum.databinding.CardReviewImageBinding

class ReviewImageListAdapter(private val clickListenerReviewImageFull: ClickListenerReviewImageFull):
    ListAdapter<String, ReviewImageListAdapter.ViewHolder>(DiffCallback){

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
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
        holder.bind(dataItem, clickListenerReviewImageFull)
    }

    class ViewHolder private constructor(private val binding: CardReviewImageBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardReviewImageBinding.inflate(layoutInflater)
                return ViewHolder(binding)
            }
        }

        fun bind(dataItem: String, clickListenerReviewImageFull: ClickListenerReviewImageFull) {
            binding.imageUri = dataItem
            binding.clickListener = clickListenerReviewImageFull
            binding.executePendingBindings()
        }
    }
}

class ClickListenerReviewImageFull(val clickListener: (imageUri: String) -> Unit) {
    fun onClick(imageUri: String) = clickListener(imageUri)
}