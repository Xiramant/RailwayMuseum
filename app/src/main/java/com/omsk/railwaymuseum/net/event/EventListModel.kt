package com.omsk.railwaymuseum.net.event

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EventListModel(
    @field:Expose
    val id: Int,

    @field:Expose
    @field:SerializedName("articles_order")
    val order: Int,

    @field:Expose
    @field:SerializedName("image_preview")
    val imageUri: String,

    @field:Expose
    val name: String,

    @field:Expose
    @field:SerializedName("short_name")
    val shortName: String,

    @field:Expose
    val text: String,

    @field:Expose
    @field:SerializedName("is_continue")
    val isContinue: Int
)