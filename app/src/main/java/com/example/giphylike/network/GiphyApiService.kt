package com.example.giphylike.network

import com.example.giphylike.model.DataResult
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApiService {
    @GET("gifs/search")
    fun getGifs(
        @Query("q") searchTerm: String,
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0,
        @Query("rating") rating: String = "g",
        @Query("lang") lang: String = "en",
        @Query("bundle") bundle: String = "messaging_non_clips",
        @Query("api_key") apiKey: String = "Nty0QwPq7qtVYaLSylNtlbtMEVzp8Gnx"
    ): retrofit2.Call<DataResult>
}