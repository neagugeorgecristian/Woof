package com.example.woof

import android.app.Application
import com.example.woof.data.AppContainer
import com.example.woof.data.DefaultAppContainer

class DogPhotosApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}