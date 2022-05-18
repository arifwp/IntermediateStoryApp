package com.awp.intermediatestoryapp.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.awp.intermediatestoryapp.R
import com.awp.intermediatestoryapp.databinding.ActivityMainBinding
import com.awp.intermediatestoryapp.model.UserModel
import com.awp.intermediatestoryapp.preference.SessionPreference
import com.awp.intermediatestoryapp.model.ViewModelFactory
import com.awp.intermediatestoryapp.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var loginViewModel : LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        instanceViewModel()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        doLogin()
    }

    private fun doLogin() {
        with(binding){
            btnLogin.setOnClickListener {
                loginViewModel.LoginAccounts(
                    loginEmail.text.toString(),
                    loginPassword.text.toString()
                )
                loginViewModel.login()
                loginViewModel.loginResponse.observe(this@LoginActivity){ login->

                    savingSessionModel(
                        UserModel(
                            "${login.loginResult?.name}",
                            "Bearer ${login.loginResult?.token}",
                            true
                        )
                    )
                    moveActivity()
                }
            }
        }
    }

    private fun instanceViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(SessionPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]
    }

    fun savingSessionModel(user: UserModel){
        loginViewModel.saveUser(user)
    }

    private fun moveActivity() {
        val i = Intent(this@LoginActivity, HomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }

}