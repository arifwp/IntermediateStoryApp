package com.awp.intermediatestoryapp.viewmodel.AddStoryViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.awp.intermediatestoryapp.api.ApiConfig
import com.awp.intermediatestoryapp.model.AddStoryResponse
import com.awp.intermediatestoryapp.model.Login.UserSessionModel
import com.awp.intermediatestoryapp.preference.SessionPreference
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class addStoryVM(private val pref: SessionPreference): ViewModel() {

    private val _uploadResponse = MutableLiveData<AddStoryResponse>()
    val uploadResponse: LiveData<AddStoryResponse> = _uploadResponse

    fun uploadStory(token:String, file: MultipartBody.Part, description: RequestBody){
        val client = ApiConfig.getApiService().postStories(token, file,description)

        client.enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(
                call: Call<AddStoryResponse>,
                response: Response<AddStoryResponse>
            ) {
                if (response.isSuccessful){
                    _uploadResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                Log.d("error upload", t.message.toString())
            }

        }
        )
    }

    fun getUser(): LiveData<UserSessionModel> {
        return pref.getUser().asLiveData()
    }

}