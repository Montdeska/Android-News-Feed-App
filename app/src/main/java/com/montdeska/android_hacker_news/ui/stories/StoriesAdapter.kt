package com.montdeska.android_hacker_news.ui.stories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.montdeska.android_hacker_news.data.local.models.Story
import com.montdeska.android_hacker_news.databinding.StoryLayoutBinding

class StoriesAdapter(private val storyClickListener: StoryClickListener) :
    ListAdapter<Story, StoriesAdapter.StoriesViewHolder>(StoriesDiffCallback()) {

    class StoriesViewHolder private constructor(private val binding: StoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): StoriesViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StoryLayoutBinding.inflate(layoutInflater, parent, false)
                return StoriesViewHolder(binding)
            }
        }

        fun bind(story: Story, storyClickListener: StoryClickListener) {
            binding.story = story
            binding.storyClickListener = storyClickListener
            binding.executePendingBindings()
        }
    }


    class StoriesDiffCallback : DiffUtil.ItemCallback<Story>() {
        override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem.story_id == newItem.story_id
        }

        override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem == newItem
        }
    }

    class StoryClickListener(val storyClickListener: (storyUrl: String) -> Unit) {
        fun onClick(item: Story) {
            val url =
                if (item.story_url != null && item.story_url.isNotEmpty()) item.story_url else ""
            storyClickListener(url)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        return StoriesViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        holder.bind(getItem(position)!!, storyClickListener)
    }

    fun getItemStory(position: Int): Story {
        return super.getItem(position)
    }
}