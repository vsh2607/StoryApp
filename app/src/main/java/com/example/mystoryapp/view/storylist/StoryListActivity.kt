package com.example.mystoryapp.view.storylist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.paging.flatMap
import com.example.mystoryapp.R
import com.example.mystoryapp.adapter.StoryListAdapter
import com.example.mystoryapp.databinding.ActivityStoryListBinding
import com.example.mystoryapp.sharedpreferences.SharedPreferencesManager
import com.example.mystoryapp.view.camera.CameraMainActivity
import com.example.mystoryapp.view.map.StoryMapActivity
import com.example.mystoryapp.view.welcome.WelcomeActivity


class StoryListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStoryListBinding
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var storyListViewModel : StoryListViewModel





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferencesManager = SharedPreferencesManager(this)

        val username = sharedPreferencesManager.getUsername()
        val token = sharedPreferencesManager.getToken()

        if(token != null){
            storyListViewModel = ViewModelProvider(this, ViewModelFactory(token))[StoryListViewModel::class.java]

        }

        getAllStories()


        if (username != null) {
            setWelcomeName(username)
        }


    }

    private fun getAllStories() {
        val adapter = StoryListAdapter()
        binding.rvStory.adapter = adapter
        storyListViewModel.story.observe(this){
            val transformedData = it.flatMap { response ->
                response.listStory
            }
            adapter.submitData(lifecycle, transformedData)
        }


    }


    private fun setWelcomeName(username : String){
        binding.tvWelcoming.text = "Welcome to StoryApp, $username  "
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.menu_add_story -> {
                startActivity(Intent(this, CameraMainActivity::class.java))
                true
            }
            R.id.menu_loc -> {
                startActivity(Intent(this, StoryMapActivity::class.java))
                true
            }

            else   -> {
                sharedPreferencesManager.clearData()
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
                true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
    }


}