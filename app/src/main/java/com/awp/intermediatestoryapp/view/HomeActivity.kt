package com.awp.intermediatestoryapp.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.awp.intermediatestoryapp.R
import com.awp.intermediatestoryapp.adapter.StoriesAdapter
import com.awp.intermediatestoryapp.databinding.ActivityHomeBinding
import com.awp.intermediatestoryapp.preference.SessionPreference
import com.awp.intermediatestoryapp.model.ViewModelFactory
import com.awp.intermediatestoryapp.viewmodel.HomeViewModel
import com.awp.intermediatestoryapp.viewmodel.home.StoryViewModel
import com.awp.intermediatestoryapp.viewmodel.home.StoryViewModelFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: ActivityHomeBinding


    private lateinit var storiesAdapter: StoriesAdapter
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val storyViewModel: StoryViewModel by viewModels {
        StoryViewModelFactory(this, SessionPreference.getInstance(dataStore))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = getString(R.string.title_home)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRv()
        instanceViewModel()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.story_activity->{
                startActivity(Intent(this, AddStoryActivity::class.java))
                true
            }
            R.id.maps_activity->{
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }
            R.id.logout_activity ->{
                homeViewModel.logout()
                startActivity(Intent(this, FirstActivity::class.java))
                finish()
                true
            }
            else -> false
        }
    }

    fun setRv(){
        storiesAdapter = StoriesAdapter()
        binding.rvStories.layoutManager = LinearLayoutManager(this)
        binding.rvStories.adapter = storiesAdapter
        storyViewModel.getAllStory.observe(this){
            storiesAdapter.submitData(lifecycle,it)
        }
    }

    private fun instanceViewModel() {
        homeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(SessionPreference.getInstance(dataStore))
        )[HomeViewModel::class.java]
    }
}