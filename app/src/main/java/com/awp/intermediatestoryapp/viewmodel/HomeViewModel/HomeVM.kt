package com.awp.intermediatestoryapp.viewmodel.HomeViewModel

import android.util.Log
import androidx.lifecycle.*
import com.awp.intermediatestoryapp.api.ApiConfig
import com.awp.intermediatestoryapp.model.Login.UserSessionModel
import com.awp.intermediatestoryapp.model.stories.StoriesResponseWithLocation
import com.awp.intermediatestoryapp.preference.SessionPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeVM(private val pref: SessionPreference) : ViewModel() {
    private val _list = MutableLiveData<StoriesResponseWithLocation>()
    val list: LiveData<StoriesResponseWithLocation> = _list

    fun getListStoriesWithLocation(token:String){
        val client = ApiConfig.getApiService().getAllStorieswithLocation(token)

        client.enqueue(object : Callback<StoriesResponseWithLocation> {
            override fun onResponse(
                call: Call<StoriesResponseWithLocation>,
                response: Response<StoriesResponseWithLocation>
            ) {
                if (response.isSuccessful){
                    _list.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<StoriesResponseWithLocation>, t: Throwable) {
                Log.d("error getlist", t.message.toString())
            }

        }
        )
    }


    fun getUser(): LiveData<UserSessionModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }


}