package com.omsk.railwaymuseum.ui.event

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omsk.railwaymuseum.databinding.CardEventListBinding
import com.omsk.railwaymuseum.net.event.EventListModel

class EventListAdapter(private val clickListener: ClickListenerEventList):
    ListAdapter<EventListModel, EventListAdapter.ViewHolder>(DiffCallback){

    companion object DiffCallback : DiffUtil.ItemCallback<EventListModel>() {
        override fun areItemsTheSame(oldItem: EventListModel, newItem: EventListModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: EventListModel, newItem: EventListModel): Boolean {
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
        holder.bind(dataItem, clickListener)
    }

    class ViewHolder private constructor(private val binding: CardEventListBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardEventListBinding.inflate(layoutInflater)
                return ViewHolder(binding)
            }
        }

        fun bind(dataItem: EventListModel, clickListener: ClickListenerEventList) {
            binding.eventListModel = dataItem
            binding.clickListenerEventList = clickListener
            binding.executePendingBindings()
        }
    }
}

class ClickListenerEventList(val clickListener: (cardView: View, eventId: String) -> Unit) {
    fun onClick(cardView: View, dataItem: EventListModel) = clickListener(cardView, dataItem.id.toString())
}