package com.example.mystoryapp.view.storylist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.api.ApiConfig
import com.example.mystoryapp.model.StoryListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryListViewModel : ViewModel() {

    private val _storyListResponse =  MutableLiveData<StoryListResponse>()
    val storyListResponse : LiveData<StoryListResponse>  = _storyListResponse

     fun getAllStory(token : String){
        ApiConfig.getApiService().getAllStories("Bearer $token").enqueue(object : Callback<StoryListResponse>{
            override fun onResponse(call: Call<StoryListResponse>, response: Response<StoryListResponse>){

                if(response.isSuccessful){
                    Log.d("TAG", response.body().toString())
                    _storyListResponse.postValue(response.body())
                }else{
                    Log.d("TAG", "GAGAL TEST")
                }
            }

            override fun onFailure(call: Call<StoryListResponse>, t: Throwable) {
                Log.d("TAG", "GAGAL")
            }

        })
    }
}