package com.example.woof.data

import com.example.woof.network.DogApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val dogPhotosRepository : DogPhotosRepository
}

class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://dog.ceo/api/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: DogApiService by lazy {
        retrofit.create(DogApiService::class.java)
    }

    override val dogPhotosRepository: DogPhotosRepository by lazy {
        NetworkDogPhotosRepository(retrofitService)
    }
}