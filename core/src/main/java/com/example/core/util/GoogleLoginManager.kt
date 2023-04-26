package com.example.core.util

import android.app.Activity
import androidx.activity.result.ActivityResult
import com.example.core.BuildConfig
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class GoogleLoginManager @Inject constructor() {

    var activity: Activity? = null

    fun initGoogleManager(activity: Activity) {
        this.activity = activity
    }

    fun getSignedInAccountFromIntent(result: ActivityResult) = GoogleSignIn.getSignedInAccountFromIntent(result.data)

    private fun getGoogleSignInOptions() =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.WEB_CLIENT_ID)
            .requestEmail()
            .build()

    fun getClient() =
        activity?.let { GoogleSignIn.getClient(it, getGoogleSignInOptions()).signInIntent }


    fun logOut(auth: FirebaseAuth) {

        activity?.let {
        GoogleSignIn.getClient(
            it,
            getGoogleSignInOptions()
        ).asGoogleApiClient()

    }?.let {
        Auth.GoogleSignInApi.signOut(
            it
        )
    }
        auth.signOut()
        activity?.finish()
    }


}

