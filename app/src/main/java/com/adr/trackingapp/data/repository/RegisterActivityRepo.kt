package com.adr.trackingapp.data.repository

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable

class RegisterActivityRepo: IRegisterActivityRepo {

    lateinit var activity: Activity

    override fun initializeRepo(activity: Activity){
        this.activity = activity
    }

    override fun signUpNewUser(email: String, password: String, auth: FirebaseAuth): Observable<Boolean> {
        var isSuccessful = false
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity){
            if (it.isSuccessful){
                isSuccessful = true
            }
        }
        return Observable.just(isSuccessful)
    }
}