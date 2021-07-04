package com.adr.trackingapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.adr.trackingapp.BuildConfig

class SessionManager() {

    private var context: Context? = null
    private val appName = BuildConfig.APPLICATION_ID + ".utils"
    private lateinit var masterKeyAlias: String
    private lateinit var sharedPreferencesEncrypted: SharedPreferences

    companion object {
        const val USERNAME = "username"
    }

    fun initializeSessionManager(context: Context){
        this.context = context
        masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        sharedPreferencesEncrypted = EncryptedSharedPreferences.create(appName, masterKeyAlias,
            context, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
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

    fun setEncryptedPref(type: SessionManagerEnum, key: String, value: Any) {
        val editor = sharedPreferencesEncrypted.edit()
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

    fun getEncryptedPref(type: SessionManagerEnum, key: String): Any {
        return when(type){
            SessionManagerEnum.BOOLEAN -> {
                sharedPreferencesEncrypted.getBoolean(key, false) as Boolean
            }
            SessionManagerEnum.STRING -> {
                sharedPreferencesEncrypted.getString(key, "") as String
            }
            SessionManagerEnum.INT -> {
                sharedPreferencesEncrypted.getInt(key, 0) as Int
            }
            SessionManagerEnum.LONG -> {
                sharedPreferencesEncrypted.getLong(key, 0) as Long
            }
        }
    }
}