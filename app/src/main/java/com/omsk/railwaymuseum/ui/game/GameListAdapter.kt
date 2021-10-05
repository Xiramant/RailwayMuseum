package com.omsk.railwaymuseum.ui.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omsk.railwaymuseum.databinding.CardGameListBinding
import com.omsk.railwaymuseum.net.game.GameListModel

class GameListAdapter(private val clickListener: ClickListenerGameList):
    ListAdapter<GameListModel, GameListAdapter.ViewHolder>(DiffCallback){

    companion object DiffCallback : DiffUtil.ItemCallback<GameListModel>() {
        override fun areItemsTheSame(oldItem: GameListModel, newItem: GameListModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: GameListModel, newItem: GameListModel): Boolean {
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

    class ViewHolder private constructor(private val binding: CardGameListBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardGameListBinding.inflate(layoutInflater)
                return ViewHolder(binding)
            }
        }

        fun bind(dataItem: GameListModel, clickListener: ClickListenerGameList) {
            binding.gameListModel = dataItem
            binding.clickListenerGameList = clickListener
            binding.executePendingBindings()
        }
    }
}

class ClickListenerGameList(val clickListener: (GameId: Int) -> Unit) {
    fun onClick(dataItem: GameListModel) = clickListener(dataItem.id)
}