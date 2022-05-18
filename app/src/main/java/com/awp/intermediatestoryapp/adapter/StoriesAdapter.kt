package com.awp.intermediatestoryapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.awp.intermediatestoryapp.databinding.LayoutStoryBinding
import com.awp.intermediatestoryapp.response.Story
import com.awp.intermediatestoryapp.view.DetailActivity
import com.bumptech.glide.Glide

class StoriesAdapter: PagingDataAdapter<Story, StoriesAdapter.storiesAdapter>(DIFF_CALLBACK) {
    inner class storiesAdapter (val binding: LayoutStoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: storiesAdapter, position: Int) {
        val story = getItem(position)
        holder.binding.tvStorytittle.text = story!!.name
        Glide.with(holder.itemView.context)
            .load(story.photoUrl)
            .fitCenter()
            .into(holder.binding.imageView)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DATA, story)
            holder.itemView.context.startActivity(intent)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): storiesAdapter {
        val binding = LayoutStoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return storiesAdapter(binding)
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>(){
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Story,
                newItem: Story
            ): Boolean {
                return oldItem.name == newItem.name &&
                        oldItem.photoUrl == newItem.photoUrl &&
                        oldItem.id == newItem.id &&
                        oldItem.createdAt == newItem.createdAt &&
                        oldItem.description == newItem.description
            }

        }
    }

}