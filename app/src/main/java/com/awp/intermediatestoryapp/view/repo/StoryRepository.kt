package com.awp.intermediatestoryapp.view.repo

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.awp.intermediatestoryapp.api.ApiService
import com.awp.intermediatestoryapp.model.stories.ListStoryItem
import com.awp.intermediatestoryapp.preference.SessionPreference
import com.awp.intermediatestoryapp.view.home.StoryPagingSource

class StoryRepository(private val apiService: ApiService, private val sessionPreference: SessionPreference) {

    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, sessionPreference)
            }
        ).liveData
    }
}