package com.example.mystoryapp.view.storylist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.R
import com.example.mystoryapp.adapter.StoryListAdapter
import com.example.mystoryapp.databinding.ActivityStoryListBinding
import com.example.mystoryapp.model.ListStoryItem
import com.example.mystoryapp.sharedpreferences.SharedPreferencesManager
import com.example.mystoryapp.view.camera.CameraMainActivity
import com.example.mystoryapp.view.storydetail.StoryDetailActivity
import com.example.mystoryapp.view.welcome.WelcomeActivity

class StoryListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStoryListBinding
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var viewModel: StoryListViewModel
    private lateinit var adapter : StoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[StoryListViewModel::class.java]
        sharedPreferencesManager = SharedPreferencesManager(this)

        val username = sharedPreferencesManager.getUsername()
        val token = sharedPreferencesManager.getToken()


        if (username != null) {
            setWelcomeName(username)
        }

        if (token != null) {
            Log.d("TAG", "TEST TOKEN : $token")
            getAllStories(token)
        }

    }

    private fun getAllStories(token: String) {
        viewModel.getAllStory(token)
        viewModel.storyListResponse.observe(this) { response ->
            val storyList = response.listStory
            setAdapter(storyList)
        }
    }

    private fun setAdapter(storyList: List<ListStoryItem>) {
        adapter = StoryListAdapter(storyList)
        binding.rvStory.layoutManager = LinearLayoutManager(this@StoryListActivity)
        binding.rvStory.adapter = adapter
        adapter.setOnClickCallBack(object : StoryListAdapter.OnItemClickCallBack{
            override fun onItemClicked(data: ListStoryItem) {
                val intent = Intent(this@StoryListActivity, StoryDetailActivity::class.java)
                intent.putExtra(StoryDetailActivity.EXTRA_USERNAME, data.name)
                intent.putExtra(StoryDetailActivity.EXTRA_IMG_URL, data.photoUrl)
                intent.putExtra(StoryDetailActivity.EXTRA_DESC, data.description)
                intent.putExtra(StoryDetailActivity.EXTRA_DATE, data.createdAt)
                startActivity(intent)
            }
        })
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
               Log.d("TAG", "Menu Settings")
                true
            }
            R.id.menu_add_story -> {
                startActivity(Intent(this, CameraMainActivity::class.java))
                true
            }

          else   -> {
                sharedPreferencesManager.clearToken()
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