package com.example.mystoryapp.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.api.LoginBody
import com.example.mystoryapp.databinding.ActivityLoginBinding
import com.example.mystoryapp.sharedpreferences.SharedPreferencesManager
import com.example.mystoryapp.view.storylist.StoryListActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var sharedPreferencesManager: SharedPreferencesManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferencesManager = SharedPreferencesManager(this)

        playAnimation()
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.etLoginEmail.validateInput(0)
        binding.etLoginPassword.validateInput(1)
        validateButtonLogin()

    }

    private fun validateButtonLogin() {
        binding.btnLoginLog.setOnClickListener {
            val email = binding.etLoginEmail
            val password = binding.etLoginPassword
            if(email.text.toString().isEmpty() || email.text.toString().isEmpty()){
                Toast.makeText(this, "Pastikan anda telah mengisi input email dan password!", Toast.LENGTH_SHORT).show()
            }

            if (email.isEmailValid && password.isPasswordValid) {
                showLoading(true)
                viewModel.postLogin(LoginBody(email.text.toString(), password.text.toString()))
                viewModel.loginResponse.observe(this) { response ->
                    if (!response.error) {
                        showLoading(false)
                        sharedPreferencesManager.saveData(response.loginResult.token, response.loginResult.name, email.text.toString())
                        startActivity(Intent(this, StoryListActivity::class.java))
                        finish()
                    } else {
                        showLoading(false)
                        Toast.makeText(this, "Login Failed. Try Again!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun playAnimation() {
        val loginText =
            ObjectAnimator.ofFloat(binding.tvLoginTitle, View.ALPHA, 1f).setDuration(500)
        val emailText =
            ObjectAnimator.ofFloat(binding.etLoginEmail, View.ALPHA, 1f).setDuration(500)
        val passText =
            ObjectAnimator.ofFloat(binding.etLoginPassword, View.ALPHA, 1f).setDuration(500)
        val loginBottom =
            ObjectAnimator.ofFloat(binding.btnLoginLog, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(emailText, passText)
        }

        AnimatorSet().apply {
            playSequentially(loginText, together, loginBottom)
            start()
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.pbProgressBar.visibility = View.VISIBLE
        } else {
            binding.pbProgressBar.visibility = View.GONE
        }
    }


}



