package com.omsk.railwaymuseum.net.game

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

interface GameListApiService {
    @GET("mobile.php?goal=games")
    suspend fun getGameListApi(): List<GameListModel>
}

object GameListApi {
    val retrofitService : GameListApiService by lazy {
        retrofit.create(GameListApiService::class.java) }
}