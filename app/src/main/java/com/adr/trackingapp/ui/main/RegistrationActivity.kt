package com.adr.trackingapp.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.adr.trackingapp.R
import com.adr.trackingapp.databinding.ActivityRegistrationBinding
import com.adr.trackingapp.ui.viewmodel.RegisterActivityViewModel
import java.util.regex.Pattern
import kotlin.math.sign

class RegistrationActivity : AppCompatActivity() {

    private var binding: ActivityRegistrationBinding? = null
    private val viewModel by lazy { RegisterActivityViewModel() }
    private var emailIsValid = false
    private var passwordIsValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)

        setContentView(binding?.root)
        viewModel.initializeViewModel(this)

        binding?.etEmailRegis?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.isNotEmpty()){
                    if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()){
                        binding?.etEmailRegis?.error = resources.getString(R.string.e_mail_regis_error)
                        emailIsValid = false
                    } else {
                        binding?.etEmailRegis?.error = null
                        emailIsValid = true
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        val passwordRegex = ("^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[!@#$%^&+=])"
                + "(?=\\S+$).{8,20}$")
        val passwordPattern = Pattern.compile(passwordRegex)
        binding?.etPasswordRegis?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.isNotEmpty()){
                    if (!passwordPattern.matcher(s).matches()){
                        binding?.etPasswordRegis?.error = resources.getString(R.string.password_regis_error)
                        passwordIsValid = false
                    } else {
                        binding?.etPasswordRegis?.error = null
                        passwordIsValid = true
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding?.btnRegisterRegis?.setOnClickListener {
            // TODO: check the re enter password
            if (binding?.etPasswordRegis?.text.toString() == binding?.etPasswordRepeatRegis?.text.toString()){
                when {
                    binding?.etFirstNameRegis?.text.isNullOrEmpty() -> {
                        Toast.makeText(this, resources.getString(R.string.first_name_regis_empty_error),
                            Toast.LENGTH_SHORT).show()
                    }
                    binding?.etLastNameRegis?.text.isNullOrEmpty() -> {
                        Toast.makeText(this, resources.getString(R.string.last_name_regis_empty_error),
                            Toast.LENGTH_SHORT).show()
                    }
                    binding?.etUsernameRegis?.text.isNullOrEmpty() -> {
                        Toast.makeText(this, resources.getString(R.string.username_regis_empty_error),
                            Toast.LENGTH_SHORT).show()
                    }
                    binding?.etEmailRegis?.text.isNullOrEmpty() -> {
                        Toast.makeText(this, resources.getString(R.string.e_mail_regis_empty_error),
                            Toast.LENGTH_SHORT).show()
                    }
                    binding?.etPasswordRegis?.text.isNullOrEmpty() -> {
                        Toast.makeText(this, resources.getString(R.string.password_regis_empty_error),
                            Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        if (emailIsValid && passwordIsValid){
                            signUpNewUser(binding?.etFirstNameRegis?.text.toString(),
                                binding?.etLastNameRegis?.text.toString(),
                                binding?.etUsernameRegis?.text.toString(),
                                binding?.etEmailRegis?.text.toString(),
                                binding?.etPasswordRegis?.text.toString())
                        }
                    }
                }
            } else {
                Toast.makeText(this, resources.getString(R.string.password_regis_not_same),
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun signUpNewUser(firstName: String, secondName:String, userName: String,
                      email: String, password: String){
        viewModel.signUpNewUser(firstName, secondName, userName, email, password)
            .observe(this, Observer {
            if (it){
                // to welcome page
                startActivity(Intent(this, SplashWelcomeActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, resources.getString(R.string.sign_up_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getActivityInstance(): Activity {
        return this
    }
}