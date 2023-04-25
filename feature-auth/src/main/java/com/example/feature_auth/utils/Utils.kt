package com.example.feature_auth.utils

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK

fun Context.navigateToActivity(activityPath: String) {
    runCatching {
        val intent = Intent(this, Class.forName(activityPath) ).apply {
            flags = FLAG_ACTIVITY_NEW_TASK
        }

        startActivity(intent)
    }.onFailure {
        it.printStackTrace()
    }
}