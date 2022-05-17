package com.awp.intermediatestoryapp.view.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.awp.intermediatestoryapp.R
import com.awp.intermediatestoryapp.databinding.ActivityRegisterBinding
import com.awp.intermediatestoryapp.view.login.LoginActivity
import com.awp.intermediatestoryapp.viewmodel.RegisterViewModel.RegisteVM

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val registeVM by viewModels<RegisteVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            btnRegister.setOnClickListener {

                registeVM.registerAccount(
                    edName.text.toString(),
                    edEmail.text.toString(),
                    edPass.text.toString()
                )
                registeVM.registerResponse.observe(this@RegisterActivity){response->

                    if (response.error){
                        Toast.makeText(this@RegisterActivity,"failed, ${response.message}", Toast.LENGTH_SHORT).show()
                    }else{
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    }


                }
            }
        }

    }
}