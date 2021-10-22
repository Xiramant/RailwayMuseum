package com.omsk.railwaymuseum.net.game

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

//Использование @Parcelize требуется для возможности передачи объекта данного класса
//      в Safe Args navigation jetpack
@Parcelize
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
): Parcelable