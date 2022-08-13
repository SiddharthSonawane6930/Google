package com.notes.catalogue.app

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication


class AppController : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        mContext = this
        application = this

//todo enable once you configure firestore
//        val db = Firebase.firestore
//        val settings = FirebaseFirestoreSettings.Builder()
//                .setPersistenceEnabled(true)
//                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
//                .build()
//        db.firestoreSettings = settings

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        var mContext: Context? = null

    }

    var application: AppController? = null

}