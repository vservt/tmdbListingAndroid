package com.example.tmdb.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.tmdb.data.repository.TmdbRepository
import com.example.tmdb.ui.components.MediaCard

@Composable
fun MoviesScreen(repository: TmdbRepository, navController: NavHostController) {
    val items = repository.moviePager().collectAsLazyPagingItems()
    LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        items(items) { item ->
            item?.let { MediaCard(it) { navController.navigate("movie/${it.id}") } }
        }
        items.apply {
            if (!loadState.append.endOfPaginationReached) {
                item { Text("Loading more...") }
            }
        }
    }
}
