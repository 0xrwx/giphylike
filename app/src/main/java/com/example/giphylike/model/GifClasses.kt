package com.example.giphylike.model

import com.google.gson.annotations.SerializedName

data class DataResult(
    @SerializedName("data") val res: List<DataObject>
)

data class DataObject(
    @SerializedName("images") val images: DataImage,
    val title: String
)

data class DataImage(
    @SerializedName("original") val source: DataSource,
    @SerializedName("fixed_height") val source_smaller: DataSource,
    @SerializedName("fixed_height_small") val source_smalest: DataSource,
)

data class DataSource(
    val url: String,
    val height: String,
    val width: String
)