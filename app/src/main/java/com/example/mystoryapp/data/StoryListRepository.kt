package com.example.mystoryapp.data

import StoryListPagingSource
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.mystoryapp.model.ListStoryItem
import kotlinx.coroutines.flow.Flow

class StoryListRepository {
    fun getAllStories(token: String): Flow<PagingData<ListStoryItem>> {
        val pagingConfig = PagingConfig(pageSize = 20)
        Log.d("TAG", "In repository : $token")

        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { StoryListPagingSource(token) }
        ).flow
    }
}
