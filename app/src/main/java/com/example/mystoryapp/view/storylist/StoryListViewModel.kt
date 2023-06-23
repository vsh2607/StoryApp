package com.example.mystoryapp.view.storylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryapp.data.StoryListRepository
import com.example.mystoryapp.model.ListStoryItem
import kotlinx.coroutines.flow.Flow

class StoryListViewModel(private val repository: StoryListRepository) : ViewModel() {

    fun getAllStory(token: String): Flow<PagingData<ListStoryItem>> {
        val allStoryFlow = repository.getAllStories(token).cachedIn(viewModelScope)


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
