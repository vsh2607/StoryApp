package com.example.mystoryapp.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.api.AuthBody
import com.example.mystoryapp.databinding.ActivityRegisterBinding
import com.example.mystoryapp.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var viewModel : RegisterVIewModel
    private var isEmailValid = false
    private var isPasswordValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[RegisterVIewModel::class.java]


        playAnimation()
        validateButtonLogin()

        binding.etRegisterEmail.addTextChangedListener(createEmailTextWatcher())
        binding.etRegisterPassword.addTextChangedListener(createPasswordTextWatcher())
    }

    private fun validateButtonLogin() {
        binding.btnRegisterReg.setOnClickListener {
            if(isEmailValid || isPasswordValid){
                showLoading(true)
                val name = binding.etRegisterName.text.toString().trim()
                val email = binding.etRegisterEmail.text.toString().trim()
                val password = binding.etRegisterPassword.text.toString().trim()
                viewModel.postRegister(AuthBody(name, email, password), this)
                viewModel.registerResponse.observe(this){
                    if(it.error == false){
                        showLoading(false)
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }else{
                        showLoading(false)
                        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }
    private fun createEmailTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            val emailText = s.toString().trim()
            if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                binding.etRegisterEmail.error = "Invalid Email address"
                isEmailValid = false
            } else if (emailText.isEmpty()) {
                binding.etRegisterEmail.error = "Email can't be empty"
                isEmailValid = false
            } else {
                isEmailValid = true
            }
        }
    }
    private fun createPasswordTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            val passText = s.toString().trim()
            if (passText.length in 1..7) {
                binding.etRegisterPassword.error = "Password can't be less than 8 chars"
                isPasswordValid = false
            } else if (passText.isEmpty()) {
                binding.etRegisterPassword.error = "Password can't be empty"
                isPasswordValid = false
            } else {
                isPasswordValid = true
            }
        }
    }
    private fun playAnimation(){

        val register_title = ObjectAnimator.ofFloat(binding.tvRegisterTitle, View.ALPHA, 1f).setDuration(500)
        val register_name = ObjectAnimator.ofFloat(binding.etRegisterName, View.ALPHA, 1f).setDuration(500)
        val register_email = ObjectAnimator.ofFloat(binding.etRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val register_password = ObjectAnimator.ofFloat(binding.etRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val register_button = ObjectAnimator.ofFloat(binding.btnRegisterReg, View.ALPHA, 1f).setDuration(500)

        val playTogether = AnimatorSet().apply{
            playTogether(register_name, register_email, register_password)
        }

        AnimatorSet().apply{
            playSequentially(register_title, playTogether, register_button)
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