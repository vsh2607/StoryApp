package com.example.mystoryapp.view.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.api.ApiConfig
import com.example.mystoryapp.api.AuthBody
import com.example.mystoryapp.model.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterVIewModel : ViewModel() {

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse : LiveData<RegisterResponse> = _registerResponse



     fun postRegister(authBody : AuthBody) {

            ApiConfig.getApiService().postRegister(authBody).enqueue(object : Callback<RegisterResponse>{
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if(response.isSuccessful){
                        _registerResponse.postValue(response.body())
                        Log.d("TAG", response.body().toString())
                    }else{
                        _registerResponse.postValue(RegisterResponse(true, "null"))
                        Log.d("TAG", "Register Response Failed")
                    }
                }
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    _registerResponse.postValue(RegisterResponse(true, "null"))
                    Log.d("TAG", t.message.toString())
                }

            })


    }

}
