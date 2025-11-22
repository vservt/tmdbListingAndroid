package com.example.tmdb.data.repository

import android.content.Context
import com.example.tmdb.data.api.MediaApi
import com.example.tmdb.data.api.ReportApi
import com.example.tmdb.data.api.TmdbApi
import com.example.tmdb.data.db.AppDatabase
import com.example.tmdb.data.db.FavoriteEntity
import com.example.tmdb.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.Query

class TmdbRepository(context: Context) {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfigHolder.tmdbBaseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val original = chain.request()
                        val url = original.url.newBuilder()
                            .addQueryParameter("api_key", BuildConfigHolder.tmdbApiKey)
                            .build()
                        val request = original.newBuilder().url(url).build()
                        chain.proceed(request)
                    }
                    .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
                    .build()
            )
            .build()
    }

    private val mediaRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfigHolder.mediaApiBaseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
    }

    private val reportRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfigHolder.reportApiBaseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
    }

    private val api: TmdbApi = retrofit.create(TmdbApi::class.java)
    private val mediaApi: MediaApi = mediaRetrofit.create(MediaApi::class.java)
    private val reportApi: ReportApi = reportRetrofit.create(ReportApi::class.java)

    private val favoritesDao = AppDatabase.get(context).favoriteDao()

    fun trendingPager(): Flow<PagingData<MediaItem>> = buildPager { page ->
        api.trending(page).results.map { it.toMediaItem() }
    }

    fun moviePager(): Flow<PagingData<MediaItem>> = buildPager { page ->
        api.discoverMovies(page).results.map { it.toMediaItem(MediaType.MOVIE) }
    }

    fun tvPager(): Flow<PagingData<MediaItem>> = buildPager { page ->
        api.discoverTv(page).results.map { it.toMediaItem(MediaType.TV) }
    }

    fun searchPager(query: String): Flow<PagingData<MediaItem>> = buildPager { page ->
        if (query.isBlank()) emptyList() else api.search(query, page).results.map { it.toMediaItem() }
    }

    suspend fun movieDetail(id: Int) = api.movieDetail(id)
    suspend fun tvDetail(id: Int) = api.tvDetail(id)
    suspend fun tvSeason(tvId: Int, season: Int) = api.tvSeason(tvId, season)
    suspend fun playStream(id: Int, type: MediaType) = mediaApi.play(id, type.name.lowercase())
    suspend fun submitReport(request: ReportRequest) = reportApi.submitReport(request)

    fun favorites(type: MediaType): Flow<List<MediaItem>> = favoritesDao.favoritesByType(type).map { list ->
        list.map { it.toMediaItem() }
    }

    suspend fun toggleFavorite(item: MediaItem) {
        favoritesDao.upsert(FavoriteEntity.from(item))
    }

    private fun buildPager(loader: suspend (page: Int) -> List<MediaItem>): Flow<PagingData<MediaItem>> =
        Pager(PagingConfig(pageSize = 20)) {
            object : PagingSource<Int, MediaItem>() {
                override fun getRefreshKey(state: PagingState<Int, MediaItem>): Int? = state.anchorPosition

                override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaItem> {
                    return try {
                        val page = params.key ?: 1
                        val data = loader(page)
                        LoadResult.Page(
                            data = data,
                            prevKey = if (page == 1) null else page - 1,
                            nextKey = if (data.isEmpty()) null else page + 1
                        )
                    } catch (t: Throwable) {
                        LoadResult.Error(t)
                    }
                }
            }
        }.flow
}

object BuildConfigHolder {
    lateinit var tmdbApiKey: String
    lateinit var tmdbBaseUrl: String
    lateinit var mediaApiBaseUrl: String
    lateinit var reportApiBaseUrl: String
}
