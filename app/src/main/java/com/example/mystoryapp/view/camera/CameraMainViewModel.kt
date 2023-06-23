package com.example.mystoryapp.view.camera

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.api.ApiConfig
import com.example.mystoryapp.model.AddNewStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CameraMainViewModel : ViewModel() {

    private val _addStoryResponse  = MutableLiveData<AddNewStoryResponse>()
    val addStoryResponse : LiveData<AddNewStoryResponse> = _addStoryResponse

    fun uploadStory(token : String, description : RequestBody, photo : MultipartBody.Part, lat : RequestBody, lon : RequestBody){

        try{
            Log.d("TAG", "$token - $description - $photo")
            ApiConfig.getApiService().uploadStory("Bearer $token", description, photo, lat, lon).enqueue(object : Callback<AddNewStoryResponse>{
                override fun onResponse(
                    call: Call<AddNewStoryResponse>,
                    response: Response<AddNewStoryResponse>
                ) {
                    if(response.isSuccessful){
                        _addStoryResponse.postValue(response.body())
                        Log.d("TAG", response.body()!!.message)
                    }else{
                        Log.d("TAG", response.message())
                    }
                }

                override fun onFailure(call: Call<AddNewStoryResponse>, t: Throwable) {
                    Log.d("TAG", t.message.toString())
                }

            })

        }catch (e : Exception){
            Log.d("TAG", e.toString())
        }


    }



    fun uploadStoryWithoutLocation(token : String, description : RequestBody, photo : MultipartBody.Part){
        try{
            Log.d("TAG", "$token - $description - $photo")
            ApiConfig.getApiService().uploadStoryWithoutLocation("Bearer $token", description, photo).enqueue(object : Callback<AddNewStoryResponse>{
                override fun onResponse(
                    call: Call<AddNewStoryResponse>,
                    response: Response<AddNewStoryResponse>
                ) {
                    if(response.isSuccessful){
                        _addStoryResponse.postValue(response.body())
                        Log.d("TAG", response.body()!!.message)
                    }else{
                        Log.d("TAG", response.message())
                    }
                }

                override fun onFailure(call: Call<AddNewStoryResponse>, t: Throwable) {
                    Log.d("TAG", t.message.toString())
                }

            })

        }catch (e : Exception){
            Log.d("TAG", e.toString())
        }
    }
}