package com.example.tmdb.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tmdb.data.model.MediaItem
import com.example.tmdb.data.model.MediaType
import com.example.tmdb.data.model.ReportRequest
import com.example.tmdb.data.model.TvSeason
import com.example.tmdb.data.repository.TmdbRepository
import com.example.tmdb.ui.components.imageUrl
import kotlinx.coroutines.launch

@Composable
fun TvDetailsScreen(id: Int, repository: TmdbRepository, navController: NavHostController) {
    val detailState = remember { mutableStateOf<com.example.tmdb.data.model.TvDetail?>(null) }
    val showReport = remember { mutableStateOf(false) }
    val reportReason = remember { mutableStateOf("") }
    val reportDescription = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    LaunchedEffect(id) {
        detailState.value = repository.tvDetail(id)
    }
    val detail = detailState.value ?: return
    val item = MediaItem(
        id = detail.id,
        title = detail.title,
        posterPath = detail.posterPath,
        backdropPath = detail.backdropPath,
        rating = detail.voteAverage,
        year = (detail.firstAirDate ?: "").take(4),
        overview = detail.overview,
        type = MediaType.TV
    )

    Column(
        modifier = Modifier.fillMaxSize().background(Color.Black).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = imageUrl(detail.backdropPath),
            contentDescription = detail.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.height(200.dp)
        )
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            AsyncImage(
                model = imageUrl(detail.posterPath),
                contentDescription = detail.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(180.dp)
            )
            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                Text(detail.title, style = MaterialTheme.typography.headlineSmall, color = Color.White)
                Text("Rating: ${detail.voteAverage}", color = Color.White)
                Text("Year: ${(detail.firstAirDate ?: "").take(4)}", color = Color.White)
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { scope.launch { repository.playStream(detail.id, MediaType.TV) } }) { Text("Watch") }
                    Button(onClick = { showReport.value = true }) { Text("Report") }
                    Button(onClick = { scope.launch { repository.toggleFavorite(item) } }) { Text("Favorite") }
                }
            }
        }
        Text(detail.overview, color = Color.White, modifier = Modifier.padding(top = 12.dp))
        Spacer(Modifier.height(16.dp))
        Text("Seasons", color = Color.White, style = MaterialTheme.typography.titleMedium)
        Column(modifier = Modifier.fillMaxWidth()) {
            detail.seasons.forEach { season ->
                SeasonRow(season) { navController.navigate("tv/${detail.id}/season/${season.seasonNumber}") }
            }
        }
    }

    if (showReport.value) {
        AlertDialog(
            onDismissRequest = { showReport.value = false },
            confirmButton = {
                TextButton(onClick = {
                    showReport.value = false
                    scope.launch {
                        repository.submitReport(
                            ReportRequest(
                                title = detail.title,
                                tmdbId = detail.id,
                                mediaType = "tv",
                                reason = reportReason.value,
                                description = reportDescription.value
                            )
                        )
                    }
                }) { Text("Report") }
            },
            dismissButton = { TextButton(onClick = { showReport.value = false }) { Text("Cancel") } },
            title = { Text("Report content") },
            text = {
                Column {
                    Text("Title: ${detail.title}")
                    Text("TMDB id: ${detail.id}")
                    Text("Type: tv")
                    androidx.compose.material3.OutlinedTextField(
                        value = reportReason.value,
                        onValueChange = { reportReason.value = it },
                        label = { Text("Reason") }
                    )
                    androidx.compose.material3.OutlinedTextField(
                        value = reportDescription.value,
                        onValueChange = { reportDescription.value = it },
                        label = { Text("Description") }
                    )
                }
            }
        )
    }
}

@Composable
fun SeasonRow(season: TvSeason, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(season.name, color = Color.White)
        Text("S${season.seasonNumber}", color = Color.White)
    }
}
