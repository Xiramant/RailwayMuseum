package com.omsk.railwaymuseum.net.game

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GameRulesModel(
        @field:Expose
        val id: Int,

        @field:Expose
        val name: String,

        @field:Expose
        val type: String,

        @field:Expose
        val rules: String,

        @field:Expose
        val comment: String,

        @field:Expose
        @field:SerializedName("questions_number")
        val questionsNumber: Int
)