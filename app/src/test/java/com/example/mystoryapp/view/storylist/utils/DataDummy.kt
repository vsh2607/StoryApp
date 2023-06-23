package com.example.mystoryapp.view.storylist.utils

import com.example.mystoryapp.model.ListStoryItem

object DataDummy {

    fun generateDummyStoryEntity() : List<ListStoryItem>{
        val storyList = ArrayList<ListStoryItem>()

        for(i in 0..10){
            val stories =   ListStoryItem("https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png", "2023-06-21T02:08:42.930Z", "henry", "no Desc", 110.4497215 ,"story-BTJyPK3TrF1osbQT", -7.7747728)
            storyList.add(stories)
        }
        return storyList
    }

}