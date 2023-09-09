package com.example.giphylike.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
fun HomeScreen(navController: NavHostController, searchData: String, onSearchDataChange: (String) -> Unit) {
    var gifs by remember { mutableStateOf(listOf<DataObject>()) }
    var isError by remember { mutableStateOf(false) }
    var lim by remember { mutableStateOf(25) }

    var expanded by remember { mutableStateOf(false) }

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retroService = retrofit.create(GiphyApiService::class.java)

    // API request
    LaunchedEffect(searchData, lim) {
        try {
            withContext(Dispatchers.IO) {
                val response = retroService.getGifs(searchTerm = searchData, limit = lim).execute()
                if (response.isSuccessful && response.body() != null) {
                    gifs = response.body()!!.res
                } else {
                    Log.e("API_ERROR", "Response unsuccessful or body is null. Response code: ${response.code()}")
                    isError = true
                }
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "Exception during API call", e)
            isError = true
        }
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = searchData,
//            onValueChange = { searchData = it },
            onValueChange = onSearchDataChange,
            label = { Text("Search") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.width(360.dp)
        )
        Box(Modifier.width(50.dp)) {
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
                    text = { Text("Limit 25") },
                    onClick = { lim = 25 }
                )
                DropdownMenuItem(
                    text = { Text("Limit 50") },
                    onClick = { lim = 50 }
                )
            }
        }

    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(gifs.chunked(2)) { _, rowGifs ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowGifs.forEach { gif ->
                    GifItem(gif, navController)
                }
            }
        }
    }
}