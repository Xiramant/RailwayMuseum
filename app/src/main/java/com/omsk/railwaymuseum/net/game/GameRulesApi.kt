package com.omsk.railwaymuseum.net.game

import com.google.gson.GsonBuilder
import com.omsk.railwaymuseum.util.BASE_URL
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private val gson = GsonBuilder()
        .setLenient()
        .create()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(BASE_URL)
        .build()

interface GameRulesApiService {
    @GET("mobile.php?goal=game_rules")
    suspend fun getGameRulesApi(@Query("id") gameNameId: Int): GameRulesModel
}

object GameRulesApi {
    val retrofitService : GameRulesApiService by lazy {
        retrofit.create(GameRulesApiService::class.java) }
}