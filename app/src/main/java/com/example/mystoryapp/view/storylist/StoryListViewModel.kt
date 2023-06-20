package com.example.mystoryapp.view.storylist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryapp.data.StoryListRepository
import com.example.mystoryapp.model.ListStoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class StoryListViewModel(private val repository: StoryListRepository) : ViewModel() {

    fun getAllStory(token: String): Flow<PagingData<ListStoryItem>> {
        val allStoryFlow = repository.getAllStories(token).cachedIn(viewModelScope)
        allStoryFlow.onEach { pagingData ->
            Log.d("TAG", "ViewModel data: $pagingData")
        }.launchIn(viewModelScope)

        return allStoryFlow
    }

    class Factory(private val repository: StoryListRepository) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StoryListViewModel::class.java)) {
                return StoryListViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}
