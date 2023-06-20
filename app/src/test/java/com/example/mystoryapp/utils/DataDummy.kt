package com.example.mystoryapp.utils

import com.example.mystoryapp.model.ListStoryItem

object DataDummy {

    fun generateDummyStoriesEntity(): List<ListStoryItem> {

        val storyList = ArrayList<ListStoryItem>()

        for (i in 0..10) {
            val story  = ListStoryItem("https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png", "2022-02-22T22:22:22Z", "Henry", "Test Desc", -7.7956, "12", 110.3695 )
            storyList.add(story)
        }

        return storyList


    }
}