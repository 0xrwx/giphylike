package com.example.giphylike.network

import com.example.giphylike.model.DataResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApiService {
    @GET("gifs/search")
    fun getSearchedGifs(
        @Query("q") searchTerm: String,
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0,
        @Query("rating") rating: String = "g",
        @Query("lang") lang: String = "en",
        @Query("bundle") bundle: String = "messaging_non_clips",
        @Query("api_key") apiKey: String = "Nty0QwPq7qtVYaLSylNtlbtMEVzp8Gnx"
    ): Call<DataResult>

    @GET("gifs/trending")
    fun getTrendingGifs(
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0,
        @Query("rating") rating: String = "g",
        @Query("bundle") bundle: String = "messaging_non_clips",
        @Query("api_key") apiKey: String = "Nty0QwPq7qtVYaLSylNtlbtMEVzp8Gnx"
    ): Call<DataResult>
}