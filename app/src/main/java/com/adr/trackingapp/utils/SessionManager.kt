package com.adr.trackingapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.adr.trackingapp.BuildConfig

class SessionManager() {

    private var context: Context? = null
    private val appName = BuildConfig.APPLICATION_ID + ".utils"

    companion object {
        const val USERNAME = "username"
    }

    fun initializeSessionManager(context: Context){
        this.context = context
    }

    fun setPreference(type: SessionManagerEnum, key: String, value: Any){
        val editor = context?.getSharedPreferences(appName, MODE_PRIVATE)?.edit()
        when(type){
            SessionManagerEnum.BOOLEAN -> {
                editor?.putBoolean(key, value as Boolean)
            }
            SessionManagerEnum.STRING -> {
                editor?.putString(key, value as String)
            }
            SessionManagerEnum.INT -> {
                editor?.putInt(key, value as Int)
            }
            SessionManagerEnum.LONG -> {
                editor?.putLong(key, value as Long)
            }
        }

        editor?.apply()
    }

    fun getPreference(type: SessionManagerEnum, key: String): Any {
        val preference = context?.getSharedPreferences(appName, MODE_PRIVATE)
        return when(type){
            SessionManagerEnum.BOOLEAN -> {
                preference?.getBoolean(key, false) as Boolean
            }
            SessionManagerEnum.STRING -> {
                preference?.getString(key, "") as String
            }
            SessionManagerEnum.INT -> {
                preference?.getInt(key, 0) as Int
            }
            SessionManagerEnum.LONG -> {
                preference?.getLong(key, 0) as Long
            }
        }
    }
}