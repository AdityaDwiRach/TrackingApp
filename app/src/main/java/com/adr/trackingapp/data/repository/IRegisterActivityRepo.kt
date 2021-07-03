package com.adr.trackingapp.data.repository

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable

interface IRegisterActivityRepo {

    fun initializeRepo(activity: Activity)

    fun signUpNewUser(email: String, password: String, auth: FirebaseAuth): Observable<Boolean>
}