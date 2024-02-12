package com.example.woof.model

import kotlinx.serialization.Serializable

@Serializable
data class DogPhoto(
    val message: List<String>,
    val status: String
)