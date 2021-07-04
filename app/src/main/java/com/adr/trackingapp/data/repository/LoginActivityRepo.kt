package com.adr.trackingapp.data.repository

import android.app.Activity
import android.content.Context
import com.adr.trackingapp.ui.main.LoginActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.PublishSubject

class LoginActivityRepo: ILoginActivityRepo {
    
    lateinit var activity: Activity

    override fun initializeRepo(activity: Activity){
        this.activity = activity
    }

    override fun signInExistingUser(email: String, password: String, auth: FirebaseAuth): Observable<Boolean> {

        return Observable.create { emitter ->
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful){
                    emitter.onNext(true)
                } else {
                    emitter.onNext(false)
                }

                emitter.onComplete()
            }
        }
    }
}