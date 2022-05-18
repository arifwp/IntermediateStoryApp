package com.awp.intermediatestoryapp.view

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.awp.intermediatestoryapp.api.ApiService
import com.awp.intermediatestoryapp.response.Story
import com.awp.intermediatestoryapp.preference.SessionPreference
import com.awp.intermediatestoryapp.view.utils.StoryPagingSource

class StoryRepository(private val apiService: ApiService, private val sessionPreference: SessionPreference) {

    fun getStory(): LiveData<PagingData<Story>> {
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