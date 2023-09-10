package com.example.giphylike.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.giphylike.BASE_URL
import com.example.giphylike.GifItem
import com.example.giphylike.R
import com.example.giphylike.model.DataObject
import com.example.giphylike.network.GiphyApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    searchData: String,
    ratingInfo: String,
    onSearchDataChange: (String) -> Unit,
    onRatingInfoChange: (String) -> Unit,
    backgroundColor: Color
) {
    var gifs by remember { mutableStateOf(listOf<DataObject>()) }

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retroService = retrofit.create(GiphyApiService::class.java)

    // API request
    LaunchedEffect(searchData, ratingInfo) {
        try {
            withContext(Dispatchers.IO) {
                val response = retroService.getGifs(searchTerm = searchData, rating = ratingInfo).execute()
                if (response.isSuccessful && response.body() != null) {
                    gifs = response.body()!!.res
                } else {
                    Log.e("API_ERROR", "Response unsuccessful or body is null. Response code: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "Exception during API call", e)
        }
    }
    TextField(
        value = searchData,
        onValueChange = onSearchDataChange,
        label = { Text("Search") },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        colors = TextFieldDefaults.textFieldColors(
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        modifier = Modifier.fillMaxWidth()
    )

    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = Modifier
            .width(50.dp)
            .offset(x = (screenWidthDp - 43).dp, y = 5.dp)
    ) {
        MainDropDownButton(onRatingInfoChange)
    }

    var offset by remember { mutableStateOf(0) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp),
    ) {
        itemsIndexed(gifs.chunked(2)) { index, rowGifs ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                rowGifs.forEach { gif ->
                    GifItem(gif, navController, backgroundColor)
                }
            }
            if (index == (gifs.size / 2) - 1) { // the last item achieved
                LaunchedEffect(offset) {
                    try {
                        withContext(Dispatchers.IO) {
                            val response = retroService.getGifs(searchTerm = searchData, limit = 50, offset = offset).execute()
                            if (response.isSuccessful && response.body() != null) {
                                gifs = gifs + response.body()!!.res // Append new gifs to existing list
                                offset += 50 // Increase the offset for the next batch
                            } else {
                                Log.e("API_ERROR", "Response unsuccessful or body is null. Response code: ${response.code()}")
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("API_ERROR", "Exception during API call", e)
                    }
                }
            }
        }
    }
}

@Composable
fun MainDropDownButton(onRatingInfoChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = !expanded }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.home_show_more_button_description)
        )
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = { Text("g") },
            onClick = { onRatingInfoChange("g") }
        )
        DropdownMenuItem(
            text = { Text("pg") },
            onClick = { onRatingInfoChange("pg") }
        )
        DropdownMenuItem(
            text = { Text("pg-13") },
            onClick = {onRatingInfoChange("pg-13") }
        )
        DropdownMenuItem(
            text = { Text("r") },
            onClick = { onRatingInfoChange("r") }
        )
    }
}