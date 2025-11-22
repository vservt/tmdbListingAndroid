package com.example.tmdb.data.model

import com.squareup.moshi.Json

enum class MediaType { MOVIE, TV }

data class TmdbPagedResponse<T>(
    val page: Int,
    @Json(name = "total_pages") val totalPages: Int,
    val results: List<T>
)

data class NetworkMedia(
    val id: Int,
    @Json(name = "title") val title: String? = null,
    @Json(name = "name") val name: String? = null,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "vote_average") val voteAverage: Double?,
    @Json(name = "release_date") val releaseDate: String? = null,
    @Json(name = "first_air_date") val firstAirDate: String? = null,
    val overview: String? = null,
    @Json(name = "media_type") val mediaType: String? = null
) {
    fun toMediaItem(forcedType: MediaType? = null): MediaItem = MediaItem(
        id = id,
        title = title ?: name.orEmpty(),
        posterPath = posterPath,
        backdropPath = backdropPath,
        rating = voteAverage ?: 0.0,
        year = (releaseDate ?: firstAirDate ?: "").take(4),
        overview = overview.orEmpty(),
        type = forcedType ?: when (mediaType) {
            "tv" -> MediaType.TV
            "movie" -> MediaType.MOVIE
            else -> MediaType.MOVIE
        }
    )
}

data class MediaItem(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val backdropPath: String?,
    val rating: Double,
    val year: String,
    val overview: String,
    val type: MediaType
)

data class MovieDetail(
    val id: Int,
    val title: String,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "release_date") val releaseDate: String?,
    val overview: String
)

data class TvDetail(
    val id: Int,
    @Json(name = "name") val title: String,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "first_air_date") val firstAirDate: String?,
    val overview: String,
    val seasons: List<TvSeason>
)

data class TvSeason(
    val id: Int,
    val name: String,
    @Json(name = "season_number") val seasonNumber: Int,
    @Json(name = "poster_path") val posterPath: String?
)

data class TvEpisode(
    val id: Int,
    val name: String,
    @Json(name = "still_path") val stillPath: String?,
    @Json(name = "episode_number") val episodeNumber: Int,
    val overview: String
)

data class MediaStream(
    @Json(name = "stream_url") val streamUrl: String
)

data class ReportRequest(
    val title: String,
    @Json(name = "tmdb_id") val tmdbId: Int,
    @Json(name = "media_type") val mediaType: String,
    val reason: String,
    val description: String
)

data class ReportResponse(
    val status: String
)
