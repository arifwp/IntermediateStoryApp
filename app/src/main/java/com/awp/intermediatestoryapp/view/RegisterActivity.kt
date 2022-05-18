package com.awp.intermediatestoryapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.awp.intermediatestoryapp.R
import com.awp.intermediatestoryapp.databinding.ActivityRegisterBinding
import com.awp.intermediatestoryapp.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        doRegister()

    }

    private fun doRegister() {
        with(binding){
            btnRegister.setOnClickListener {

                registerViewModel.registerAccount(
                    registerName.text.toString(),
                    registerEmail.text.toString(),
                    registerPassword.text.toString()
                )
                registerViewModel.registerResponse.observe(this@RegisterActivity){response->

                    if (response.error){
                        Toast.makeText(this@RegisterActivity,"Failed registration, ${response.message}", Toast.LENGTH_SHORT).show()
                    }else{
                        moveActivity()
                    }


                }
            }
        }
    }

    private fun moveActivity() {
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        finish()
    }
}