package com.example.tmdb.data.api

import com.example.tmdb.data.model.ReportRequest
import com.example.tmdb.data.model.ReportResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportApi {
    @POST("report")
    suspend fun submitReport(@Body request: ReportRequest): ReportResponse
}
