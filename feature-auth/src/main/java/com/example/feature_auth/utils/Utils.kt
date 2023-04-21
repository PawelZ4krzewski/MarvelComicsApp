package com.example.feature_auth.utils

import android.content.Context
import android.content.Intent

fun Context.navigateToActivity(activityPath: String) {
    runCatching {
        val intent = Intent(this, Class.forName(activityPath) )
        startActivity(intent)
    }.onFailure {
        it.printStackTrace()
    }
}