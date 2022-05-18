package com.awp.intermediatestoryapp.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.awp.intermediatestoryapp.preference.SessionPreference
import com.awp.intermediatestoryapp.viewmodel.AddStoryViewModel.addStoryViewModel
import com.awp.intermediatestoryapp.viewmodel.HomeViewModel
import com.awp.intermediatestoryapp.viewmodel.LoginViewModel

class ViewModelFactory(private val pref: SessionPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java)->{
                HomeViewModel(pref) as T
            }
            modelClass.isAssignableFrom(addStoryViewModel::class.java)->{
                addStoryViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}