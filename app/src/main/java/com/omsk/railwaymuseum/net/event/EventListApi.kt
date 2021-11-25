package com.omsk.railwaymuseum.net.event

import com.google.gson.GsonBuilder
import com.omsk.railwaymuseum.util.PHP_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val gson = GsonBuilder()
    .setLenient()
    .create()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .baseUrl(PHP_URL)
    .build()

interface EventListApiService {
    @GET("section_list.php")
    suspend fun getEventListApi(@Query("type") goalName: String): List<EventListModel>
}

object EventListApi {
    val retrofitService : EventListApiService by lazy {
        retrofit.create(EventListApiService::class.java) }
}