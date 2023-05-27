package com.example.mystoryapp.view.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.mystoryapp.databinding.ActivityWelcomeBinding
import com.example.mystoryapp.view.login.LoginActivity
import com.example.mystoryapp.view.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit  var binding : ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playAnimation()

        binding.btnRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }




    private fun playAnimation(){

        val welcome_title = ObjectAnimator.ofFloat(binding.tvWelcomeStoryapp, View.ALPHA, 1f).setDuration(500)
        val welcome_desc = ObjectAnimator.ofFloat(binding.tvWelcomeStoryappDescription, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)

        val together =  AnimatorSet().apply  {
            playTogether(login, register)
        }

        AnimatorSet().apply {
            playSequentially(welcome_title, welcome_desc, together)
            start()
        }

    }
}