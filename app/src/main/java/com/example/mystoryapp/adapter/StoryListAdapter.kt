package com.example.mystoryapp.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystoryapp.databinding.StoryItemBinding
import com.example.mystoryapp.model.ListStoryItem
import java.text.SimpleDateFormat
import java.util.Locale


class StoryListAdapter :
    PagingDataAdapter<ListStoryItem, StoryListAdapter.ViewHolder>(STORY_COMPARATOR) {

    private var onItemClickCallBack: OnItemClickCallBack? = null

    fun setOnClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }
    interface OnItemClickCallBack {
        fun onItemClicked(data: ListStoryItem)
    }

    inner class ViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: ListStoryItem?) {
            story?.let {
                binding.apply {
                    Glide.with(itemView).load(story.photoUrl).into(ivStoryListPic)
                    tvStoryListUsername.text = story.name

                    val dateFormat =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    val date = dateFormat.parse(story.createdAt)
                    val formattedDate =
                        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
                    tvStoryListDate.text = formattedDate
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
        holder.itemView.setOnClickListener {
            onItemClickCallBack?.onItemClicked(story!!)
        }
    }

    companion object {
        private val STORY_COMPARATOR = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
