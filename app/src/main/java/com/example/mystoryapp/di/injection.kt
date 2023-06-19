package com.example.mystoryapp.di

import com.example.mystoryapp.api.ApiConfig
import com.example.mystoryapp.data.StoryRepository

object Injection {

    fun provideRepository( token : String) : StoryRepository{
        val apiService = ApiConfig.getApiService()
        return StoryRepository(apiService, token)
    }
}