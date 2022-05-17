package com.awp.intermediatestoryapp.preference

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.awp.intermediatestoryapp.viewmodel.AddStoryViewModel.addStoryVM
import com.awp.intermediatestoryapp.viewmodel.HomeViewModel.HomeVM
import com.awp.intermediatestoryapp.viewmodel.LoginViewModel.LoginVM

class ViewModelFactory(private val pref:SessionPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginVM::class.java) -> {
                LoginVM(pref) as T
            }
            modelClass.isAssignableFrom(HomeVM::class.java)->{
                HomeVM(pref) as T
            }
            modelClass.isAssignableFrom(addStoryVM::class.java)->{
                addStoryVM(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}