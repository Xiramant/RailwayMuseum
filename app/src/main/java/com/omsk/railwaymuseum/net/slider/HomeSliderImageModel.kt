package com.omsk.railwaymuseum.net.slider

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HomeSliderImageModel(
        @field:Expose
        val uri: String,

        @field:Expose
        @field:SerializedName("data")
        val dataLastChange: String,

        @field:Expose
        val size: String,
)
