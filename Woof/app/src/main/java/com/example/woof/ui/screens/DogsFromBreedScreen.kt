package com.example.woof.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.woof.R
import com.example.woof.model.DogPhoto

@Composable
fun DogsFromBreedScreen(
    dogViewModel: DogViewModel,
    onLikeButtonClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val flow by dogViewModel.uiState.collectAsStateWithLifecycle(DogUiState.Loading)

    when (flow) {
        is DogUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is DogUiState.Success -> DogsFromBreedGrid(
            (flow as DogUiState.Success).photos,
            onLikeButtonClicked,
            dogViewModel,
            modifier = modifier
        )
        is DogUiState.Error -> ErrorScreen(modifier = modifier.fillMaxSize())
    }
}

@Composable
fun DogsFromBreedGrid(
    photos: DogPhoto,
    onLikeButtonClicked: (String) -> Unit,
    dogViewModel: DogViewModel,
    modifier : Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(items = photos.message) { photo ->
            DogImageCard(
                photo,
                onLikeButtonClicked,
                dogViewModel.isFavorite(photo),
                modifier = modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .aspectRatio(1.5f)
            )
        }
    }
}

@Composable
fun DogImageCard(
    photo: String,
    onClick: (String) -> Unit,
    isFavorite: Boolean,
    modifier: Modifier = Modifier
) {
    var clicked by remember { mutableStateOf(isFavorite) }
    Card (
        modifier = modifier,
        elevation = CardDefaults.cardElevation()
    ) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(photo)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = stringResource(R.string.dog_photo),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            FavoritesButton(
                clicked = clicked,
                onClick = {
                    clicked = !clicked
                    onClick(photo)
                }
            )
        }
    }
}

@Composable
fun FavoritesButton(
    clicked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box {
        Button(
            onClick = { onClick() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .widthIn(min = 50.dp)
        ) {
            Image(
                painterResource(if (clicked) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24),
                contentDescription = null
            )
        }
    }
}