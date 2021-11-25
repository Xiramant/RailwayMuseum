package com.omsk.railwaymuseum.ui.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omsk.railwaymuseum.databinding.CardReviewBinding
import com.omsk.railwaymuseum.net.review.ReviewModel
import com.omsk.railwaymuseum.util.showFullscreenImage

class ReviewAdapter(private val clickListenerDetail: ClickListenerReviewDetail,
                    private val fragment: ReviewFragment):
    ListAdapter<ReviewModel, ReviewAdapter.ViewHolder>(DiffCallback){

    companion object DiffCallback : DiffUtil.ItemCallback<ReviewModel>() {
        override fun areItemsTheSame(oldItem: ReviewModel, newItem: ReviewModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ReviewModel, newItem: ReviewModel): Boolean {
            return oldItem.id == newItem.id
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
        holder.bind(dataItem, clickListenerDetail, fragment)
    }

    class ViewHolder private constructor(private val binding: CardReviewBinding)
                                        : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardReviewBinding.inflate(layoutInflater)
                return ViewHolder(binding)
            }
        }

        fun bind(dataItem: ReviewModel,
                clickListenerReviewDetail: ClickListenerReviewDetail,
                 fragment: ReviewFragment) {
            binding.reviewModel = dataItem
            binding.clickListener = clickListenerReviewDetail
            binding.reviewImagesRecycler.adapter = ReviewImageListAdapter(ClickListenerReviewImageFull {
                showFullscreenImage(fragment, it)
            })
            binding.reviewImagesRecycler.setHasFixedSize(true)
            binding.executePendingBindings()
        }
    }
}

class ClickListenerReviewDetail(val clickListener: (reviewId: Int) -> Unit) {
    fun onClick(reviewId: Int) = clickListener(reviewId)
}