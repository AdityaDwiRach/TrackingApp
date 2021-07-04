package com.adr.trackingapp.data.repository

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observer

interface ILoginActivityRepo {

    fun initializeRepo(activity: Activity)

    fun signInExistingUser(email: String, password: String, auth: FirebaseAuth,
                           observer: Observer<Boolean>)
}