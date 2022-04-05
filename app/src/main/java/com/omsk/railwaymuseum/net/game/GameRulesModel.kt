package com.omsk.railwaymuseum.net.game

import android.os.Parcelable
import com.omsk.railwaymuseum.data.GameType
import kotlinx.parcelize.Parcelize

//Использование @Parcelize требуется для возможности передачи объекта данного класса
//      в Safe Args navigation jetpack
@Parcelize
data class GameRulesModel(
        val id: Int,
        val name: String,
        val type: GameType,
        val rules: String,
        val comment: String,
        val questionsNumber: Int,
        val isShuffle: Int
): Parcelable

fun getEmptyGameRulesModel() : GameRulesModel {
        return GameRulesModel(-1, "", GameType.UNDEFINED, "", "", -1, -1)
}