package me.kirimin.annictroid

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics

class Annictroid : Application() {

    lateinit var analytics: FirebaseAnalytics

    override fun onCreate() {
        super.onCreate()
        analytics = FirebaseAnalytics.getInstance(this)
    }
}