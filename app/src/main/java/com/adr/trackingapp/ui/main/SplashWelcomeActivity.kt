package com.adr.trackingapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adr.trackingapp.R
import com.adr.trackingapp.databinding.ActivitySplashWelcomeBinding

class SplashWelcomeActivity : AppCompatActivity() {

    var binding: ActivitySplashWelcomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashWelcomeBinding.inflate(layoutInflater)

        setContentView(binding?.root)
    }
}