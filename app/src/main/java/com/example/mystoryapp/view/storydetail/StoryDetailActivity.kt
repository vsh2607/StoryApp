package com.example.mystoryapp.view.storydetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.mystoryapp.databinding.ActivityStoryDetailBinding

class StoryDetailActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_IMG_URL = "extra_img_url"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_DATE = "extra_date"
    }
    private lateinit var binding : ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val img_url = intent.getStringExtra(EXTRA_IMG_URL)
        val desc = intent.getStringExtra(EXTRA_DESC)
        val date = intent.getStringExtra(EXTRA_DATE)

        getStoryDetail(username, img_url, desc)

    }


    private fun getStoryDetail(username : String?, img_url : String?, desc : String?){
        Glide.with(this).load(img_url).into(binding.ivStoryDetailPic)
        binding.tvStoryDetailUsername.text = username
        binding.tvStoryDetailDesc.text = desc
    }
}