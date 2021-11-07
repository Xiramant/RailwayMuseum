package com.omsk.railwaymuseum.net.review

import com.google.gson.GsonBuilder
import com.omsk.railwaymuseum.util.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

private val gson = GsonBuilder()
    .setDateFormat("yyyy-MM-dd HH:mm:ss")
    .setLenient()
    .create()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .baseUrl(BASE_URL)
    .build()

interface ReviewApiService {
    @GET("mobile.php?goal=review_list")
    suspend fun getReviewApi(): List<ReviewModel>
}

object ReviewApi {
    val retrofitService : ReviewApiService by lazy {
        retrofit.create(ReviewApiService::class.java) }
}

interface ReviewInsertApiService {
    @FormUrlEncoded
    @POST("mobile.php")
    suspend fun insertReviewApi(@Field("goals") goals: String,
                                @Field("name") name: String,
                                @Field("review") review: String): String
}

object ReviewInsertApi {
    val retrofitInsertService : ReviewInsertApiService by lazy {
        retrofit.create(ReviewInsertApiService::class.java) }
}