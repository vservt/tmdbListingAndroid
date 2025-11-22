package com.example.tmdb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tmdb.data.repository.BuildConfigHolder
import com.example.tmdb.data.repository.TmdbRepository
import com.example.tmdb.ui.screens.FavoritesScreen
import com.example.tmdb.ui.screens.HomeScreen
import com.example.tmdb.ui.screens.MovieDetailsScreen
import com.example.tmdb.ui.screens.MoviesScreen
import com.example.tmdb.ui.screens.SeasonScreen
import com.example.tmdb.ui.screens.TvDetailsScreen
import com.example.tmdb.ui.screens.TvScreen
import com.example.tmdb.ui.theme.TmdbTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BuildConfigHolder.tmdbApiKey = BuildConfig.TMDB_API_KEY
        BuildConfigHolder.tmdbBaseUrl = BuildConfig.TMDB_BASE_URL
        BuildConfigHolder.mediaApiBaseUrl = BuildConfig.MEDIA_API_BASE_URL
        BuildConfigHolder.reportApiBaseUrl = BuildConfig.REPORT_API_BASE_URL
        val repository = TmdbRepository(this)
        setContent {
            TmdbTheme {
                val navController = rememberNavController()
                val searchQuery = remember { mutableStateOf("") }
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text("TMDB") }, actions = {})
                    },
                    bottomBar = {
                        BottomNavigationBar(navController)
                    }
                ) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(padding)
                    ) {
                        composable("home") {
                            HomeScreen(repository, navController, searchQuery)
                        }
                        composable("movies") {
                            MoviesScreen(repository, navController)
                        }
                        composable("tv") {
                            TvScreen(repository, navController)
                        }
                        composable("favorites") {
                            FavoritesScreen(repository, navController)
                        }
                        composable("movie/{id}") { backStack ->
                            val id = backStack.arguments?.getString("id")?.toIntOrNull() ?: return@composable
                            MovieDetailsScreen(id, repository, navController)
                        }
                        composable("tv/{id}") { backStack ->
                            val id = backStack.arguments?.getString("id")?.toIntOrNull() ?: return@composable
                            TvDetailsScreen(id, repository, navController)
                        }
                        composable("tv/{id}/season/{season}") { backStack ->
                            val id = backStack.arguments?.getString("id")?.toIntOrNull() ?: return@composable
                            val season = backStack.arguments?.getString("season")?.toIntOrNull() ?: return@composable
                            SeasonScreen(id, season, repository, navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val entries = navController.currentBackStackEntryAsState()
    val destination = entries.value?.destination?.route
    BottomAppBar {
        NavigationBarItem(
            selected = destination == "home",
            onClick = { navController.navigate("home") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors()
        )
        NavigationBarItem(
            selected = destination == "movies",
            onClick = { navController.navigate("movies") },
            icon = { Icon(Icons.Default.Movie, contentDescription = "Movies") },
            label = { Text("Movies") }
        )
        NavigationBarItem(
            selected = destination == "tv",
            onClick = { navController.navigate("tv") },
            icon = { Icon(Icons.Default.Tv, contentDescription = "TV") },
            label = { Text("TV") }
        )
        NavigationBarItem(
            selected = destination == "favorites",
            onClick = { navController.navigate("favorites") },
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
            label = { Text("Favorites") }
        )
    }
}
