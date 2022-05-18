package com.awp.intermediatestoryapp.view.utils

import android.content.Context
import com.awp.intermediatestoryapp.api.ApiConfig
import com.awp.intermediatestoryapp.preference.SessionPreference
import com.awp.intermediatestoryapp.view.StoryRepository

object Injection {
    fun provideRepository(context: Context, sessionPreference: SessionPreference) : StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository(apiService, sessionPreference)
    }
}