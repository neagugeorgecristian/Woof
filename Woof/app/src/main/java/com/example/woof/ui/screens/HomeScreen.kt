package com.example.woof.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.woof.R
import com.example.woof.model.DogBreeds
import com.example.woof.ui.theme.WoofTheme

@Composable
fun HomeScreen(
    linksUiState: LinksUiState,
    onImageButtonClicked: (String) -> Unit,
    onFavoritesButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    when (linksUiState) {
        is LinksUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is LinksUiState.Success -> DogBreedsList(
            dogBreeds = linksUiState.breeds,
            onImageButtonClicked,
            onFavoritesButtonClicked
            )
        is LinksUiState.Error -> ErrorScreen(modifier = modifier.fillMaxSize())
    }
}

@Composable
fun DogBreedsList(
    dogBreeds: DogBreeds,
    onImageButtonClicked: (String) -> Unit,
    onFavoritesButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (modifier = modifier) {
        FavoritesButton(
            onClick = { onFavoritesButtonClicked() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        )
        LazyColumn {
            items(dogBreeds.message.keys.toList()) { breedName ->
                val subBreeds = dogBreeds.message[breedName] ?: emptyList()
                DogBreedItem(
                    breedName = breedName,
                    subBreeds = subBreeds,
                    onImageButtonClicked,
                    modifier = Modifier
                        .widthIn(min = 50.dp, max = 80.dp)
                        .padding(4.dp)
                )
            }
        }
    }
}

@Composable
fun DogBreedItem(
    breedName: String,
    subBreeds: List<String>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column (modifier = Modifier.padding(4.dp)) {
        Button(onClick = { onClick(breedName) }) {
            Text(
                text = breedName,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        if (subBreeds.isNotEmpty()) {
            SubspeciesList(subspeciesList = subBreeds)
        }
    }
}

@Composable
fun SubspeciesList(subspeciesList: List<String>) {
    Column {
        subspeciesList.forEach { subspecies ->
            Text(
                text = " - $subspecies",
                modifier = Modifier.padding(start = 16.dp),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun FavoritesButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_favorite_24),
            contentDescription = null,
            modifier = Modifier.width(25.dp)
        )
        Text("Favorites")
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(id = R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = stringResource(R.string.connection_error_text)
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    WoofTheme {
        LoadingScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    WoofTheme {
        ErrorScreen()
    }
}