package com.example.mystoryapp.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.api.AuthBody
import com.example.mystoryapp.databinding.ActivityRegisterBinding
import com.example.mystoryapp.view.login.LoginActivity


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var viewModel : RegisterVIewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[RegisterVIewModel::class.java]

        playAnimation()
        binding.etRegisterEmail.validateInput(0)
        binding.etRegisterPassword.validateInput(1)
        binding.etRegisterName.validateInput(2)
        validateButtonLogin()

    }

    private fun validateButtonLogin() {
      binding.btnRegisterReg.setOnClickListener {

          val email = binding.etRegisterEmail
          val password = binding.etRegisterPassword
          val username = binding.etRegisterName
          if(email.text.toString().isEmpty() || password.text.toString().isEmpty() || username.text.toString().isEmpty()){
              Toast.makeText(this, "Pastikan anda telah mengisi input email, username, dan password!", Toast.LENGTH_SHORT).show()
          }

          if(email.isEmailValid && password.isPasswordValid && username.isUsernameValid){
              showLoading(true)
              viewModel.postRegister(AuthBody(username.text.toString(), email.text.toString(), password.text.toString()))

              viewModel.registerResponse.observe(this){
                  Log.d("TAG", "Hasil Register : $it")
                  if(!it.error){
                      showLoading(false)
                      startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                      finish()
                  }else{
                      showLoading(false)
                      Toast.makeText(this, "Register Failed. Try again!", Toast.LENGTH_SHORT).show()
                  }
              }

          }

      }
    }
    private fun playAnimation(){

        val registerTitle = ObjectAnimator.ofFloat(binding.tvRegisterTitle, View.ALPHA, 1f).setDuration(500)
        val registerName = ObjectAnimator.ofFloat(binding.etRegisterName, View.ALPHA, 1f).setDuration(500)
        val registerEmail = ObjectAnimator.ofFloat(binding.etRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val registerPassword = ObjectAnimator.ofFloat(binding.etRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val registerButton = ObjectAnimator.ofFloat(binding.btnRegisterReg, View.ALPHA, 1f).setDuration(500)

        val playTogether = AnimatorSet().apply{
            playTogether(registerName, registerEmail, registerPassword)
        }

        AnimatorSet().apply{
            playSequentially(registerTitle, playTogether, registerButton)
            start()
        }

    }
    private fun showLoading(state : Boolean){
        if(state){
            binding.pbProgressBar.visibility = View.VISIBLE
        }else{
            binding.pbProgressBar.visibility = View.GONE
        }
    }



}


