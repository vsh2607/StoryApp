package com.example.mystoryapp.view.storylist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryapp.api.ApiConfig
import com.example.mystoryapp.data.StoryRepository
import com.example.mystoryapp.di.Injection
import com.example.mystoryapp.model.ListStoryItem
import com.example.mystoryapp.model.StoryListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryListViewModel(storyRepository: StoryRepository) : ViewModel() {

        val story : LiveData<PagingData<StoryListResponse>> =
                storyRepository.getStory().cachedIn(viewModelScope)

}

class ViewModelFactory(private val token : String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryListViewModel(Injection.provideRepository(token)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}