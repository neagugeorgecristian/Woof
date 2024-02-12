package com.example.woof.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.woof.DogPhotosApplication
import com.example.woof.data.DogPhotosRepository
import com.example.woof.model.DogBreeds
import com.example.woof.model.DogPhoto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface DogUiState {
    data class Success(val photos: DogPhoto) : DogUiState
    object Error : DogUiState
    object Loading : DogUiState
}

sealed interface LinksUiState {
    data class Success(val breeds: DogBreeds) : LinksUiState
    object Error : LinksUiState
    object Loading : LinksUiState
}

class DogViewModel(
    private val dogPhotosRepository: DogPhotosRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<DogUiState> = MutableStateFlow(DogUiState.Loading)
    val uiState: Flow<DogUiState> = _uiState

    var linksUiState : LinksUiState by mutableStateOf(LinksUiState.Loading)
        private set

    var selectedDogs : List<String> by mutableStateOf(getInitialFavoriteDogs())
        private set

    init {
        getBreedsList()
    }

    private fun getInitialFavoriteDogs() : List<String> {
        return emptyList()
    }

    fun addToFavorites(dog: String) {
        if(!selectedDogs.contains(dog))  selectedDogs += dog
        else selectedDogs -= dog
    }

    fun isFavorite(dog: String) : Boolean {
        return selectedDogs.contains(dog)
    }

    fun extractBreedName(url: String): String {
        val breedsIndex = url.indexOf("breeds/") + "breeds/".length
        val breedSubPath = url.substring(breedsIndex)
        val endIndex = breedSubPath.indexOf('/')
        return breedSubPath.substring(0, endIndex)
    }

    private fun getDogPhotos(breedName: String) {
        viewModelScope.launch {
            _uiState.emit(DogUiState.Loading)
            _uiState.emit(try {
                val test = dogPhotosRepository.getDogPhotos(breedName)
                Log.d("DogUiState inside getDogPhotos is: ",_uiState.emit(DogUiState.Loading).toString())
                DogUiState.Success(test)
            } catch (e: IOException) {
                DogUiState.Error
            } catch (e: HttpException) {
                DogUiState.Error
            })
        }
    }

    fun onDogBreedSelect(dogBreed: String) {
        getDogPhotos(dogBreed)
    }

    private fun getBreedsList() {
        viewModelScope.launch {
            linksUiState = LinksUiState.Loading
            linksUiState = try {
                LinksUiState.Success(dogPhotosRepository.getBreedsList())
            } catch (e: IOException) {
                LinksUiState.Error
            } catch (e: HttpException) {
                LinksUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as DogPhotosApplication)
                val dogPhotosRepository = application.container.dogPhotosRepository
                DogViewModel(dogPhotosRepository)
            }
        }
    }
}

