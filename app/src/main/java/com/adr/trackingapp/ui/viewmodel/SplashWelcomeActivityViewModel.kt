package com.adr.trackingapp.ui.viewmodel

import android.content.Context
import com.adr.trackingapp.utils.SessionManager
import com.adr.trackingapp.utils.SessionManager.Companion.USERNAME
import com.adr.trackingapp.utils.SessionManagerEnum

class SplashWelcomeActivityViewModel {

//    private lateinit var context: Context
    private lateinit var sessionManager: SessionManager

    fun initializeViewModel(context: Context){
        sessionManager = SessionManager()
        sessionManager.initializeSessionManager(context)
    }

    fun getUserName(): String {
        return sessionManager.getEncryptedPref(SessionManagerEnum.STRING, USERNAME) as String
    }
}