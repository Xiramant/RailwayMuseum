package com.omsk.railwaymuseum.net.slider

import com.google.gson.GsonBuilder
import com.omsk.railwaymuseum.util.PHP_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private val gson = GsonBuilder()
        .setLenient()
        .create()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(PHP_URL)
        .build()

interface HomeSliderImageApiService {
    @GET("slider_images.php")
    suspend fun getEventApi(): List<HomeSliderImageModel>
}

object HomeSliderImageApi {
    val retrofitService : HomeSliderImageApiService by lazy {
        retrofit.create(HomeSliderImageApiService::class.java) }
}