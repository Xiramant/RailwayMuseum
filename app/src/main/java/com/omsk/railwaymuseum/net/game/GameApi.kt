package com.omsk.railwaymuseum.net.game

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

interface GameListApiService {
    @GET("games.php")
    suspend fun getGameListApi(): List<GameListModelNet>
}

interface GameRulesApiService {
    @GET("game_rules.php")
    suspend fun getGameRulesApi(@Query("id") gameNameId: Int): GameRulesModelNet
}

interface GameQuestionsApiService {
    @GET("game_questions.php")
    suspend fun getGameQuestionsApi(@Query("id") gameNameId: Int): List<GameQuestionsModel>
}

object GameApi {
    val retrofitListService : GameListApiService by lazy {
        retrofit.create(GameListApiService::class.java) }

    val retrofitRulesService : GameRulesApiService by lazy {
        retrofit.create(GameRulesApiService::class.java) }

    val retrofitQuestionService : GameQuestionsApiService by lazy {
        retrofit.create(GameQuestionsApiService::class.java) }
}