package com.adr.trackingapp.data.repository

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable

interface ILoginActivityRepo {

    fun initializeRepo(activity: Activity)

    fun signInExistingUser(email: String, password: String, auth: FirebaseAuth): Observable<Boolean>
}