package com.awp.intermediatestoryapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.awp.intermediatestoryapp.R
import com.awp.intermediatestoryapp.databinding.ActivityDetailBinding
import com.awp.intermediatestoryapp.response.Story
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.detail_page)

        withBinding()
    }

    private fun withBinding() {
        val data = intent.getParcelableExtra<Story>(EXTRA_DATA) as Story
        with(binding){
            nameTitle.text = data.name
            descTitle.text = data.description
            Glide.with(this@DetailActivity)
                .load(data.photoUrl)
                .into(imageStory)
        }
    }

    companion object{
        const val EXTRA_DATA = ""
    }
}