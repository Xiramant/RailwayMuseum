package com.omsk.railwaymuseum.net.review

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class ReviewModel(
    @field:Expose
    val id: Int,

    @field:Expose
    val nickname: String,

    @field:Expose
    val text: String,

    @field:Expose
    @field:SerializedName("image")
    val imageUri: String,

    @field:Expose
    @field:SerializedName("date_modify")
    val data: Timestamp
)