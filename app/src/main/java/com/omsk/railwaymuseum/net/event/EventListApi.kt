package com.omsk.railwaymuseum.net.event

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

interface EventListApiService {
    @GET("mobile.php?goal=event_list")
    suspend fun getEventListApi(): List<EventListModel>
}

object EventListApi {
    val retrofitService : EventListApiService by lazy {
        retrofit.create(EventListApiService::class.java) }
}