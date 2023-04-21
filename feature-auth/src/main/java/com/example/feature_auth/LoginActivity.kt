package com.example.feature_auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.core.util.Constants.Companion.MAIN_ACTIVITY_PATH
import com.example.feature_auth.utils.navigateToActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

//    @Inject lateinit var auth: FirebaseAuth

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        isCurrentUser()
    }

    private fun isCurrentUser(){
        if(auth.currentUser != null){
            applicationContext.navigateToActivity(MAIN_ACTIVITY_PATH)
        }
    }
}