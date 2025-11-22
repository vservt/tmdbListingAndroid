package com.example.tmdb.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tmdb.data.repository.TmdbRepository
import com.example.tmdb.ui.components.imageUrl

@Composable
fun SeasonScreen(tvId: Int, season: Int, repository: TmdbRepository, navController: NavHostController) {
    val seasonState = remember { mutableStateOf<com.example.tmdb.data.api.TvSeasonEpisodes?>(null) }
    LaunchedEffect(tvId, season) {
        seasonState.value = repository.tvSeason(tvId, season)
    }
    val data = seasonState.value ?: return
    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        Text(data.name)
        LazyColumn {
            items(data.episodes) { episode ->
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    AsyncImage(model = imageUrl(episode.stillPath), contentDescription = episode.name)
                    Text("E${episode.episodeNumber} - ${episode.name}")
                    Text(episode.overview)
                }
            }
        }
    }
}
