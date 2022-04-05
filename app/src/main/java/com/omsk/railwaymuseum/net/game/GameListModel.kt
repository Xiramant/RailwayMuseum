package com.omsk.railwaymuseum.net.game

import com.omsk.railwaymuseum.data.GameType

data class GameListModel(
        val id: Int,
        val name: String,
        val imagePreview: String,
        val type: GameType,
        val typeDescription: String,
        val difficulty: Int,
        val order: Int
)