package com.example.tmdb.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.tmdb.data.repository.TmdbRepository
import com.example.tmdb.ui.components.MediaCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

@Composable
fun HomeScreen(repository: TmdbRepository, navController: NavHostController, queryState: MutableState<String>) {
    val searchFlow = remember { MutableStateFlow("") }
    val scope = rememberCoroutineScope()
    val searchPager = searchFlow.flatMapLatest { repository.searchPager(it) }
    val trendingPager = repository.trendingPager()
    val isSearching = queryState.value.isNotBlank()
    val listItems = if (isSearching) searchPager.collectAsLazyPagingItems() else trendingPager.collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = queryState.value,
            onValueChange = {
                queryState.value = it
                searchFlow.value = it
            },
            label = { Text("Search") },
            modifier = Modifier.padding(bottom = 12.dp)
        )
        androidx.compose.foundation.lazy.LazyColumn {
            items(listItems) { item ->
                item?.let {
                    MediaCard(it) { navController.navigate("${if (it.type.name == "TV") "tv" else "movie"}/${it.id}") }
                }
            }
            listItems.apply {
                when {
                    loadState.append.endOfPaginationReached -> Unit
                    else -> item { Text("Loading more...") }
                }
            }
        }
    }
}
