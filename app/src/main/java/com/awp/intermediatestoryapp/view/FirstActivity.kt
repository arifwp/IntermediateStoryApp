package com.awp.intermediatestoryapp.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.awp.intermediatestoryapp.databinding.ActivityFirstBinding
import com.awp.intermediatestoryapp.preference.SessionPreference
import com.awp.intermediatestoryapp.model.ViewModelFactory
import com.awp.intermediatestoryapp.viewmodel.HomeViewModel

class FirstActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstBinding
    private lateinit var homeViewModel: HomeViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        instanceHomeModel()
        instanceViewModel()
        withBinding()
        startAnimation()

    }

    private fun instanceHomeModel() {
        homeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(SessionPreference.getInstance(dataStore))
        )[HomeViewModel::class.java]
    }

    private fun startAnimation() {

        val textYour = ObjectAnimator.ofFloat(binding.imgIlustrationLogin, View.ALPHA, 1f).setDuration(500)
        val textShare = ObjectAnimator.ofFloat(binding.buttonLogin, View.ALPHA, 1f).setDuration(500)
        val textStory = ObjectAnimator.ofFloat(binding.buttonRegister, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(textShare,textYour,textStory)
            start()
        }
    }

    private fun instanceViewModel() {
        homeViewModel.getUser().observe(this){
            if(it.token.isNotEmpty()){
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
    }

    private fun withBinding() {
        with(binding){
            buttonLogin.setOnClickListener {
                startActivity(Intent(this@FirstActivity, LoginActivity::class.java))
            }
            buttonRegister.setOnClickListener {
                startActivity(Intent(this@FirstActivity, RegisterActivity::class.java))
            }
        }
    }
}