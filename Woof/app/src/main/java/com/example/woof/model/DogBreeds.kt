package com.example.woof.model

import kotlinx.serialization.Serializable

@Serializable
data class DogBreeds(
    val message: Map<String, List<String>>,
    val status: String
)