package com.example.serotoninenglish20.api

import com.example.serotoninenglish20.screens.guess.CheckValid
import com.example.serotoninenglish20.screens.guess.Description
import com.example.serotoninenglish20.screens.guess.Filters
import com.example.serotoninenglish20.screens.guess.Sentence
import com.example.serotoninenglish20.screens.topics.Topic
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


private const val BASE_URL = "http://english.serotonin.site/api/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface ApiService {
    @GET("themes/all")
    suspend fun getTopics(): List<Topic>

    @GET("sentences/{theme}")
    suspend fun getSentence(
        @Path("theme") theme: String
    ): Sentence

    @GET("sentences/types")
    suspend fun getFilters(): Filters

    @GET("themes/present-simple")
    suspend fun getDescription():Description

    @FormUrlEncoded
    @POST("sentences/check")
    suspend fun checkAnswer(
        @Field("sentence") sentence: String,
        @Field("words") words: String,
    ): CheckValid
}

object Api {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}