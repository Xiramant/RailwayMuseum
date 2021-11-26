package com.omsk.railwaymuseum.net.game

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GameListModel(
        @field:Expose
        val id: Int,

        @field:Expose
        val name: String,

        @field:Expose
        @field:SerializedName("image_preview")
        val imagePreview: String,

        @field:Expose
        val type: String,

        @field:Expose
        @field:SerializedName("type_description")
        val typeDescription: String,

        @field:Expose
        val difficulty: Int,

        @field:Expose
        @field:SerializedName("games_order")
        val order: Int
)