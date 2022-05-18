package com.awp.intermediatestoryapp.view.utils

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.awp.intermediatestoryapp.api.ApiService
import com.awp.intermediatestoryapp.response.Story
import com.awp.intermediatestoryapp.preference.SessionPreference
import kotlinx.coroutines.flow.first

class StoryPagingSource(private val apiService: ApiService, private val sessionPreference: SessionPreference) : PagingSource<Int, Story>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val position = params.key ?: PAGE
            val token = sessionPreference.getDataUser().first().token
            if (token.isNotEmpty()){
                val responseData = apiService.getAllStories(token, position,params.loadSize)
                if (responseData.isSuccessful){
                    Log.d("dataStory", "load: ${responseData.body()}")
                    LoadResult.Page(
                        data = responseData.body()?.listStory ?: emptyList(),
                        prevKey = if (position == PAGE) null else position - 1,
                        nextKey = if (responseData.body()?.listStory.isNullOrEmpty()) null else position+1
                    )
                }else{
                    LoadResult.Error(Exception("Fail Load Data"))
                }
            }else{
                LoadResult.Error(Exception("Fail Load Data"))
            }


        }catch (e: Exception){
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object{
        const val PAGE = 1
    }
}