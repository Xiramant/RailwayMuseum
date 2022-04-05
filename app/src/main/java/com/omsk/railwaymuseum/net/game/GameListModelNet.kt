package com.omsk.railwaymuseum.net.game

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.omsk.railwaymuseum.data.GameType

data class GameListModelNet(
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

fun List<GameListModelNet>.asGameListModel(): List<GameListModel> {
        return map {
                GameListModel(
                        id = it.id,
                        name = it.name,
                        imagePreview = it.imagePreview,
                        type = gameTypeConvert(it.type),
                        typeDescription = it.typeDescription,
                        difficulty = it.difficulty,
                        order = it.order
                )
        }
}

fun gameTypeConvert(type: String): GameType {
        for (typeName in GameType.values()) {
                if (type.equals(typeName.toString(), ignoreCase = true)) {
                        return typeName
                }
        }
        return GameType.UNDEFINED
}