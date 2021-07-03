package com.adr.trackingapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adr.trackingapp.data.repository.LoginActivityRepo
import com.adr.trackingapp.ui.main.LoginActivity
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

    fun initializeViewModel(){
        auth = Firebase.auth
        repo.initializeRepo(LoginActivity().getActivityInstance())
    }

    fun isLoggedIn(): Boolean{
        val currentUser = auth.currentUser
        if (currentUser != null){
            return true
        }

        return false
    }

    fun signInExistingUser(email: String, password: String): MutableLiveData<Boolean>{
        var isSuccessful: MutableLiveData<Boolean> = MutableLiveData(false)
        repo.signInExistingUser(email, password, auth).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<Boolean>{
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
        return isSuccessful
    }

    fun getCurrentUser(): FirebaseUser{
        return auth.currentUser!!
    }
}