package com.example.woof.data

import com.example.woof.model.DogBreeds
import com.example.woof.model.DogPhoto
import com.example.woof.network.DogApiService

interface DogPhotosRepository {
    suspend fun getDogPhotos(breed: String) : DogPhoto
    suspend fun getBreedsList() : DogBreeds
}

class NetworkDogPhotosRepository (
    private val dogApiService: DogApiService
) : DogPhotosRepository {
    override suspend fun getDogPhotos(breed: String): DogPhoto {
        val test = dogApiService.getPhotos(breed)
        return test
    }
    override suspend fun getBreedsList(): DogBreeds = dogApiService.getBreeds()
}