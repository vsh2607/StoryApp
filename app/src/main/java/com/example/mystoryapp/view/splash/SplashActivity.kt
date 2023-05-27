package com.example.mystoryapp.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.mystoryapp.databinding.ActivitySplashBinding
import com.example.mystoryapp.sharedpreferences.SharedPreferencesManager
import com.example.mystoryapp.view.storylist.StoryListActivity
import com.example.mystoryapp.view.welcome.WelcomeActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    private lateinit var sharedPreferencesManager: SharedPreferencesManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        sharedPreferencesManager = SharedPreferencesManager(this)
        Log.d("TAG", sharedPreferencesManager.getToken().toString())
        Handler(Looper.getMainLooper()).postDelayed({
            checkTokenExist()
        }, 1000)

    }

    private fun checkTokenExist(){
        if(sharedPreferencesManager.getToken() == null){
            Log.d("TAG", "No Token")
            val intent = Intent(this@SplashActivity, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }else{
            Log.d("TAG", sharedPreferencesManager.getToken()!!)
            val intent = Intent(this@SplashActivity, StoryListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }


}