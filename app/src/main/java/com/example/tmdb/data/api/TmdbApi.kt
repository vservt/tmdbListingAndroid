package com.example.tmdb.data.api

import com.example.tmdb.data.model.*
import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    @GET("trending/all/day")
    suspend fun trending(@Query("page") page: Int): TmdbPagedResponse<NetworkMedia>

    @GET("discover/movie")
    suspend fun discoverMovies(@Query("page") page: Int): TmdbPagedResponse<NetworkMedia>

    @GET("discover/tv")
    suspend fun discoverTv(@Query("page") page: Int): TmdbPagedResponse<NetworkMedia>

    @GET("search/multi")
    suspend fun search(@Query("query") query: String, @Query("page") page: Int): TmdbPagedResponse<NetworkMedia>

    @GET("movie/{id}")
    suspend fun movieDetail(@Path("id") id: Int): MovieDetail

    @GET("tv/{id}")
    suspend fun tvDetail(@Path("id") id: Int): TvDetail

    @GET("tv/{id}/season/{seasonNumber}")
    suspend fun tvSeason(
        @Path("id") tvId: Int,
        @Path("seasonNumber") seasonNumber: Int
    ): TvSeasonEpisodes
}

data class TvSeasonEpisodes(
    val id: Int,
    @Json(name = "season_number") val seasonNumber: Int,
    val name: String,
    val episodes: List<TvEpisode>
)
