package com.example.mystoryapp.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.api.ApiConfig
import com.example.mystoryapp.api.LoginBody
import com.example.mystoryapp.model.LoginResponse
import com.example.mystoryapp.model.LoginResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse : LiveData<LoginResponse> = _loginResponse

     fun postLogin(loginBody: LoginBody){
        ApiConfig.getApiService().postLogin(loginBody).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    Log.d("TAG", response.body().toString())
                    _loginResponse.postValue(response.body())
                }else{
                    Log.d("TAG", "Login Response Failed")
                    _loginResponse.postValue(LoginResponse(true, "null", LoginResult("null", "null", "null") ))

                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("TAG", t.message.toString())
                _loginResponse.postValue(LoginResponse(true, "null", LoginResult("null", "null", "null") ))
            }

        })
    }
}