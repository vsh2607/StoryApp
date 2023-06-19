package com.example.mystoryapp.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.mystoryapp.api.ApiService
import com.example.mystoryapp.model.StoryListResponse

class StoryRepository(private val apiService: ApiService, private val token : String) {

    fun getStory() :  LiveData<PagingData<StoryListResponse>>{

        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData

    }
}