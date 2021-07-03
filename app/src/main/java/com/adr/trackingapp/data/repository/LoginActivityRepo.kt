package com.adr.trackingapp.data.repository

import android.app.Activity
import android.content.Context
import com.adr.trackingapp.ui.main.LoginActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable

class LoginActivityRepo: ILoginActivityRepo {
    
    lateinit var activity: Activity

    override fun initializeRepo(activity: Activity){
        this.activity = activity
    }

    override fun signInExistingUser(email: String, password: String, auth: FirebaseAuth):
            Observable<Boolean> {
        var isSuccessful = false
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity) {
            if (it.isSuccessful){
                isSuccessful = true
            }
        }
        return Observable.just(isSuccessful)
    }
}