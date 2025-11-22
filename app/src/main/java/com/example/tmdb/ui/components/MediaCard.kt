package com.example.tmdb.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.tmdb.data.model.MediaItem

@Composable
fun MediaCard(item: MediaItem, onClick: () -> Unit) {
    Card(modifier = Modifier.clickable { onClick() }.padding(8.dp)) {
        Row(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = imageUrl(item.posterPath),
                contentDescription = item.title,
                modifier = Modifier.size(96.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(item.title, style = MaterialTheme.typography.titleMedium)
                Text("Rating: ${item.rating}")
                Text(item.year)
            }
        }
    }
}

fun imageUrl(path: String?): String? = path?.let { "https://image.tmdb.org/t/p/w500$it" }
