package com.example.tmdb.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tmdb.data.model.MediaType
import com.example.tmdb.data.repository.TmdbRepository
import com.example.tmdb.ui.components.MediaCard

@Composable
fun FavoritesScreen(repository: TmdbRepository, navController: NavHostController) {
    val tabs = listOf(MediaType.MOVIE, MediaType.TV)
    val selected = remember { mutableStateOf(MediaType.MOVIE) }
    val favorites by repository.favorites(selected.value).collectAsState(initial = emptyList())

    TabRow(selectedTabIndex = tabs.indexOf(selected.value)) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = tab == selected.value,
                onClick = { selected.value = tab },
                text = { Text(tab.name) }
            )
        }
    }
    LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        items(favorites.size) { idx ->
            val item = favorites[idx]
            MediaCard(item) {
                navController.navigate("${if (item.type == MediaType.TV) "tv" else "movie"}/${item.id}")
            }
        }
    }
}
