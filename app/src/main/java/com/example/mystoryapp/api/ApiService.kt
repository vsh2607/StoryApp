package com.example.mystoryapp.api

import com.example.mystoryapp.model.AddNewStoryResponse
import com.example.mystoryapp.model.LoginResponse
import com.example.mystoryapp.model.RegisterResponse
import com.example.mystoryapp.model.StoryListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {


    @POST("register")
     fun postRegister(
        @Body authBody : AuthBody
    ) : Call<RegisterResponse>

    @POST("login")
     fun postLogin(
        @Body authLoginBody: LoginBody
    ) : Call<LoginResponse>

    @GET("stories")
     fun getAllStories(
        @Header("Authorization") authKey : String,
        @Query("page") page : Int? = 1,
        @Query("size") size : Int? = 10
    ) : Call<StoryListResponse>


    @Multipart
    @POST("stories")
     fun uploadStory(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,

    ): Call<AddNewStoryResponse>



}