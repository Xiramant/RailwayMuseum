package com.omsk.railwaymuseum.net

import com.google.gson.GsonBuilder
import com.omsk.railwaymuseum.util.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private val gson = GsonBuilder()
        .setLenient()
        .create()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(BASE_URL)
        .build()

interface HomeSliderImageApiService {
    @GET("mobile.php?goal=home_slider_images")
    suspend fun getEventApi(): List<HomeSliderImageModel>
}

object HomeSliderImageApi {
    val retrofitService : HomeSliderImageApiService by lazy {
        retrofit.create(HomeSliderImageApiService::class.java) }
}