package com.example.mystoryapp.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context : Context) {

    private val sharedPreferences : SharedPreferences = context.getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
    private val editor : SharedPreferences.Editor = sharedPreferences.edit()

    fun saveData(token : String, name : String, email : String){
        editor.putString("token", token)
        editor.putString("username", name)
        editor.putString("email", email)
        editor.apply()
    }

    fun getToken() : String?{
        return sharedPreferences.getString("token", null)
    }

    fun getUsername() : String?{
        return sharedPreferences.getString("username", null)
    }



    fun clearData(){
        editor.remove("token")
        editor.remove("username")
        editor.remove("email")
        editor.apply()
    }
}