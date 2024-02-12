package com.example.woof.network

import com.example.woof.model.DogBreeds
import com.example.woof.model.DogPhoto
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApiService {
    @GET("breed/{breed}/images/")
    suspend fun getPhotos(@Path("breed") breed: String): DogPhoto

    @GET("breeds/list/all/")
    suspend fun getBreeds(): DogBreeds
}
