package com.example.mystoryapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystoryapp.databinding.StoryItemBinding
import com.example.mystoryapp.model.ListStoryItem
import java.text.SimpleDateFormat
import java.util.Locale

class StoryListAdapter(private val storyList: List<ListStoryItem>) : RecyclerView.Adapter<StoryListAdapter.ViewHolder>() {

    private  var onItemClickCallBack : OnItemClickCallBack? = null

    fun setOnClickCallBack(onItemClickCallBack: OnItemClickCallBack){
        this.onItemClickCallBack = onItemClickCallBack
    }
    interface OnItemClickCallBack {
        fun onItemClicked(data : ListStoryItem)
    }

    inner class ViewHolder(private val binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.apply {
                Glide.with(itemView).load(story.photoUrl).into(ivStoryListPic)
                tvStoryListUsername.text = story.name

                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val date = dateFormat.parse(story.createdAt)
                val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
                tvStoryListDate.text = formattedDate

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(storyList[position])
        holder.itemView.setOnClickListener {
            onItemClickCallBack?.onItemClicked(storyList[position])
        }
    }

    override fun getItemCount(): Int = storyList.size
}