package com.example.mystoryapp.view.storylist

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.R
import com.example.mystoryapp.adapter.StoryListAdapter
import com.example.mystoryapp.data.StoryListRepository
import com.example.mystoryapp.databinding.ActivityStoryListBinding
import com.example.mystoryapp.model.ListStoryItem
import com.example.mystoryapp.sharedpreferences.SharedPreferencesManager
import com.example.mystoryapp.view.camera.CameraMainActivity
import com.example.mystoryapp.view.map.StoryMapActivity
import com.example.mystoryapp.view.storydetail.StoryDetailActivity
import com.example.mystoryapp.view.welcome.WelcomeActivity
import com.example.mystoryapp.view.widget.ImageBannerWidget
import kotlinx.coroutines.launch

class StoryListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryListBinding
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var viewModel: StoryListViewModel
    private lateinit var adapter: StoryListAdapter
    private lateinit var repository: StoryListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferencesManager = SharedPreferencesManager(this)
        repository = StoryListRepository()
        val viewModelFactory = StoryListViewModel.Factory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(StoryListViewModel::class.java)

        sharedPreferencesManager = SharedPreferencesManager(this)

        val username = sharedPreferencesManager.getUsername()
        val token = sharedPreferencesManager.getToken()

        if (username != null) {
            setWelcomeName(username)
        }

        if (token != null) {
            Log.d("TAG", "TEST TOKEN: $token")
            setupRecyclerView(token)
        }
    }
    private fun setupRecyclerView(token : String) {
        adapter = StoryListAdapter()
        binding.rvStory.layoutManager = LinearLayoutManager(this@StoryListActivity)
        binding.rvStory.adapter = adapter

        lifecycleScope.launch {
            viewModel.getAllStory(token).collect { pagingData ->
                Log.d("TAG", "Test entering lifecyclescope")
                pagingData.map {
                    Log.d("TAG", " data : $it")
                }
                adapter.submitData(pagingData)
            }
        }




        adapter.setOnClickCallBack(object : StoryListAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: ListStoryItem) {
                val intent = Intent(this@StoryListActivity, StoryDetailActivity::class.java)
                intent.putExtra(StoryDetailActivity.EXTRA_USERNAME, data.name)
                intent.putExtra(StoryDetailActivity.EXTRA_IMG_URL, data.photoUrl)
                intent.putExtra(StoryDetailActivity.EXTRA_DESC, data.description)
                intent.putExtra(StoryDetailActivity.EXTRA_DATE, data.createdAt)

                val widgetIntent = Intent(this@StoryListActivity, ImageBannerWidget::class.java)
                widgetIntent.action = ImageBannerWidget.ACTION_UPDATE_WIDGET
                widgetIntent.putExtra(ImageBannerWidget.EXTRA_IMAGE_URL, data.photoUrl)
                widgetIntent.putExtra(ImageBannerWidget.EXTRA_DESCRIPTION, data.description)
                sendBroadcast(widgetIntent)

                startActivity(intent)
            }
        })
    }


    private fun setWelcomeName(username: String) {
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
