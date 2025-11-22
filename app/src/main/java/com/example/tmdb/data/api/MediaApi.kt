package com.example.tmdb.data.api

import com.example.tmdb.data.model.MediaStream
import retrofit2.http.GET
import retrofit2.http.Query

interface MediaApi {
    @GET("play")
    suspend fun play(
        @Query("tmdb_id") tmdbId: Int,
        @Query("media_type") mediaType: String
    ): MediaStream
}
