package com.awp.intermediatestoryapp.api

import com.awp.intermediatestoryapp.response.AddStoryResponse
import com.awp.intermediatestoryapp.response.LoginResponse
import com.awp.intermediatestoryapp.response.RegisterResponse
import com.awp.intermediatestoryapp.response.StoriesLocationResponse
import com.awp.intermediatestoryapp.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>


    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<StoriesResponse>

    @Multipart
    @POST("stories")
    fun addStories(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<AddStoryResponse>

    @GET("stories")
    fun getStoriesLocation(
        @Header("Authorization") token: String,
        @Query("location") loc : Int = 1
    ): Call<StoriesLocationResponse>
}