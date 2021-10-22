package com.omsk.railwaymuseum.net.game

import com.google.gson.GsonBuilder
import com.omsk.railwaymuseum.util.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val gson = GsonBuilder()
        .setLenient()
        .create()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(BASE_URL)
        .build()

interface GameQuestionsApiService {
    @GET("mobile.php?goal=game_questions")
    suspend fun getGameQuestionsApi(@Query("id") gameNameId: Int): List<GameQuestionsModel>
}

object GameQuestionsApi {
    val retrofitService : GameQuestionsApiService by lazy {
        retrofit.create(GameQuestionsApiService::class.java) }
}