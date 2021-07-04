package com.adr.trackingapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adr.trackingapp.data.repository.LoginActivityRepo
import com.adr.trackingapp.ui.main.LoginActivity
import com.adr.trackingapp.utils.SessionManager
import com.adr.trackingapp.utils.SessionManager.Companion.E_MAIL
import com.adr.trackingapp.utils.SessionManager.Companion.PASSWORD
import com.adr.trackingapp.utils.SessionManager.Companion.REMEMBER_ME
import com.adr.trackingapp.utils.SessionManagerEnum
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoginActivityViewModel: ViewModel() {

    private lateinit var auth: FirebaseAuth
    private val repo by lazy { LoginActivityRepo() }
    private val compositeDisposable = CompositeDisposable()
    private lateinit var sessionManager: SessionManager

    fun initializeViewModel(context: Context){
        auth = Firebase.auth
        repo.initializeRepo(LoginActivity().getActivityInstance())
        sessionManager = SessionManager()
        sessionManager.initializeSessionManager(context)
    }

    fun isLoggedIn(): Boolean{
        val currentUser = auth.currentUser
        if (currentUser != null){
            return true
        }

        return false
    }

    fun signInExistingUser(email: String, password: String,
                           observer: androidx.lifecycle.Observer<Boolean>){

        repo.signInExistingUser(email, password, auth).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<Boolean>{
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(t: Boolean) {
                    observer.onChanged(t)
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }

            })
    }

    fun getCurrentUser(): FirebaseUser{
        return auth.currentUser!!
    }

    fun onRememberMeChecked(email: String, password: String){
        sessionManager.setEncryptedPref(SessionManagerEnum.STRING, E_MAIL, email)
        sessionManager.setEncryptedPref(SessionManagerEnum.STRING, PASSWORD, password)
    }

    fun onRememberMeNotChecked() {
        sessionManager.setEncryptedPref(SessionManagerEnum.STRING, E_MAIL, "")
        sessionManager.setEncryptedPref(SessionManagerEnum.STRING, PASSWORD, "")
    }

    fun getRememberedEmail(): String {
        return sessionManager.getEncryptedPref(SessionManagerEnum.STRING, E_MAIL) as String
    }

    fun getRememberedPassword(): String {
        return sessionManager.getEncryptedPref(SessionManagerEnum.STRING, PASSWORD) as String
    }

    fun onRememberMeStatus(status: Boolean) {
        sessionManager.setPreference(SessionManagerEnum.BOOLEAN, REMEMBER_ME, status)
    }

    fun getRememberMeStatus(): Boolean {
        return sessionManager.getPreference(SessionManagerEnum.BOOLEAN, REMEMBER_ME) as Boolean
    }
}