package com.omsk.railwaymuseum.net.game

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GameQuestionsModel(
        @field:Expose
        val id: Int,

        @field:Expose
        val question: String,

        @field:Expose
        @field:SerializedName("question_image")
        val image: String,

        @field:Expose
        val answer: String,

        val quizAnswer: List<String>
)