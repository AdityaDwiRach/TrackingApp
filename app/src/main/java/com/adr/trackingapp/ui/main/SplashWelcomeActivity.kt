package com.adr.trackingapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.adr.trackingapp.R
import com.adr.trackingapp.databinding.ActivitySplashWelcomeBinding
import com.adr.trackingapp.ui.viewmodel.SplashWelcomeActivityViewModel

class SplashWelcomeActivity : AppCompatActivity() {

    var binding: ActivitySplashWelcomeBinding? = null
    private val viewModel by lazy { SplashWelcomeActivityViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashWelcomeBinding.inflate(layoutInflater)

        setContentView(binding?.root)
        viewModel.initializeViewModel(this)

        val userName = viewModel.getUserName()
        binding?.tvSplashUsername?.text = userName.capitalize()

        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        handler.postDelayed(runnable, 3000)
    }
}