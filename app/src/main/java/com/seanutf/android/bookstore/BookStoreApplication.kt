package com.seanutf.android.bookstore

import android.app.Application
import com.seanutf.android.bookstore.data.AppContainer
import com.seanutf.android.bookstore.data.AppDataContainer

class BookStoreApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
