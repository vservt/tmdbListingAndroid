package com.example.tmdb.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tmdb.data.model.MediaItem
import com.example.tmdb.data.model.MediaType

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String?,
    val backdropPath: String?,
    val rating: Double,
    val year: String,
    val mediaType: MediaType
) {
    fun toMediaItem(): MediaItem = MediaItem(
        id = id,
        title = title,
        posterPath = posterPath,
        backdropPath = backdropPath,
        rating = rating,
        year = year,
        overview = "",
        type = mediaType
    )

    companion object {
        fun from(item: MediaItem) = FavoriteEntity(
            id = item.id,
            title = item.title,
            posterPath = item.posterPath,
            backdropPath = item.backdropPath,
            rating = item.rating,
            year = item.year,
            mediaType = item.type
        )
    }
}
