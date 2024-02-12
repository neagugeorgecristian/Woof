package com.example.woof.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.woof.R
import com.example.woof.ui.screens.DogViewModel
import com.example.woof.ui.screens.DogsFromBreedScreen
import com.example.woof.ui.screens.FavoritesScreen
import com.example.woof.ui.screens.HomeScreen

enum class WoofScreen(@StringRes val title: Int) {
    Home(title = R.string.dog_breeds),
    Favorites(title = R.string.favorites),
    Dogs(title = R.string.dogs_from_breed)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WoofAppBar(
    currentScreen: WoofScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(
            text = stringResource(currentScreen.title),
        ) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WoofApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = WoofScreen.valueOf(
        backStackEntry?.destination?.route ?: WoofScreen.Home.name
    )

    val dogViewModel: DogViewModel =
        viewModel(factory = DogViewModel.Factory)

    Scaffold(
        topBar = {
            WoofAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) {innerPadding ->

        NavHost(
            navController = navController,
            startDestination = WoofScreen.Home.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = WoofScreen.Home.name) {
                Surface {
//                    val dogViewModel: DogViewModel =
//                        viewModel(factory = DogViewModel.Factory)
                    HomeScreen(
                        linksUiState = dogViewModel.linksUiState,
                        onImageButtonClicked = {dogBreed ->
                            dogViewModel.onDogBreedSelect(dogBreed)
                            navController.navigate(WoofScreen.Dogs.name)
                        },
                        onFavoritesButtonClicked = {
                            navController.navigate(WoofScreen.Favorites.name)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    )
                }
            }
            composable(route = WoofScreen.Dogs.name) {
                Surface {
                    DogsFromBreedScreen(
                        dogViewModel = dogViewModel,
                        onLikeButtonClicked = { dogViewModel.addToFavorites(it) },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    )
                }
            }
            composable(route = WoofScreen.Favorites.name) {
                FavoritesScreen(
                    dogViewModel = dogViewModel,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }
        }
    }
}