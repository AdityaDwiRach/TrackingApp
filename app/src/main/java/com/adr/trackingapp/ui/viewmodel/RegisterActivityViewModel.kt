package com.adr.trackingapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.adr.trackingapp.data.repository.RegisterActivityRepo
import com.adr.trackingapp.ui.main.RegistrationActivity
import com.adr.trackingapp.utils.SessionManager
import com.adr.trackingapp.utils.SessionManager.Companion.USERNAME
import com.adr.trackingapp.utils.SessionManagerEnum
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RegisterActivityViewModel {

    private lateinit var auth: FirebaseAuth
    private val repo by lazy { RegisterActivityRepo() }
    private val compositeDisposable = CompositeDisposable()
    private lateinit var sessionManager: SessionManager

    fun initializeViewModel(context: Context){
        auth = Firebase.auth
        repo.initializeRepo(RegistrationActivity().getActivityInstance())
        sessionManager = SessionManager()
        sessionManager.initializeSessionManager(context)
    }

    fun signUpNewUser(firstName: String, secondName:String, userName: String,
                      email: String, password: String): MutableLiveData<Boolean> {
        var isSuccessful: MutableLiveData<Boolean> = MutableLiveData(false)
        repo.signUpNewUser(email, password, auth).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<Boolean> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(t: Boolean) {
                    isSuccessful = MutableLiveData(t)
                }

                override fun onError(e: Throwable) {
                    TODO("Not yet implemented")
                }

                override fun onComplete() {
                    TODO("Not yet implemented")
                }

            })

        sessionManager.setEncryptedPref(SessionManagerEnum.STRING, USERNAME, userName)
        return isSuccessful
    }
}