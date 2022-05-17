package com.awp.intermediatestoryapp.viewmodel.HomeViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.awp.intermediatestoryapp.model.stories.ListStoryItem
import com.awp.intermediatestoryapp.preference.SessionPreference
import com.awp.intermediatestoryapp.view.home.Injection
import com.awp.intermediatestoryapp.view.repo.StoryRepository

class StoryViewModel(private val storyRepository: StoryRepository): ViewModel() {
    val getAllStory: LiveData<PagingData<ListStoryItem>> = storyRepository.getStory().cachedIn(viewModelScope)
}
class StoryViewModelFactory(private val context: Context, private val sessionPreference: SessionPreference) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryViewModel(Injection.provideRepository(context,sessionPreference)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}