package com.adr.trackingapp.ui.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.Observer
import com.adr.trackingapp.R
import com.adr.trackingapp.databinding.ActivityLoginBinding
import com.adr.trackingapp.ui.viewmodel.LoginActivityViewModel

class LoginActivity : AppCompatActivity() {

    private val viewModel by lazy { LoginActivityViewModel() }
    private var binding: ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding?.root)
        viewModel.initializeViewModel()

        binding?.etEmailLogin?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.isNotEmpty()){
                    if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()){
                        binding?.etEmailLogin?.error = resources.getString(R.string.e_mail_regis_error)
                    } else {
                        binding?.etEmailLogin?.error = null
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding?.btnLoginLogin?.setOnClickListener {

        }

        binding?.btnGoogleLoginLogin?.setOnClickListener {

        }

        binding?.tvSignUp?.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }

    fun signInExistingUser(email: String, password: String){
        viewModel.signInExistingUser(email, password).observe(this, Observer {
            if (it){
                // to welcome page
                viewModel.getCurrentUser()
            } else {
                Toast.makeText(this, resources.getString(R.string.sign_in_email_pass_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getActivityInstance(): Activity{
        return this
    }
}