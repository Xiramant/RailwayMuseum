package com.omsk.railwaymuseum.net.review

import com.google.gson.GsonBuilder
import com.omsk.railwaymuseum.util.BASE_URL
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

private val gson = GsonBuilder()
    .setDateFormat("yyyy-MM-dd HH:mm:ss")
    .setLenient()
    .create()

private val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS).build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .baseUrl(BASE_URL)
    .client(client)
    .build()

interface ReviewApiService {
    @GET("mobile.php?goal=review_list")
    suspend fun getReviewApi(): List<ReviewModel>
}

interface ReviewInsertApiService {
    @FormUrlEncoded
    @POST("scripts/mobile/review_insert.php")
    suspend fun insertReviewApi(@Field("name") name: String,
                                @Field("review") review: String,
                                @Field("android_id") androidId: String,
                                @Field("images") images: String,
    ): String
}

interface ReviewUploadApiService {
    @Multipart
    @POST("scripts/mobile/review_upload_image.php")
    suspend fun uploadFileApi(@Part file: MultipartBody.Part?,
                              @Part("file") name: RequestBody?
    ): String
}

object ReviewApi {
    val retrofitService : ReviewApiService by lazy {
        retrofit.create(ReviewApiService::class.java) }

    val retrofitInsertService : ReviewInsertApiService by lazy {
        retrofit.create(ReviewInsertApiService::class.java) }

    val retrofitUploadService : ReviewUploadApiService by lazy {
        retrofit.create(ReviewUploadApiService::class.java) }
}