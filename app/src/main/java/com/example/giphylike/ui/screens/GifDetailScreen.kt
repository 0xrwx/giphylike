package com.example.giphylike.ui.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder

@OptIn(ExperimentalCoilApi::class)
@Composable
fun GifDetailScreen(gifUrl: String, navController: NavHostController) {
    val context = LocalContext.current
    var isFullSize: Boolean by remember { mutableStateOf(true) }

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .componentRegistry { add(GifDecoder()) }
            .build()
    }

    Log.d("GifDetailScreen", "Received gif URL: $gifUrl")
    val decodedUrl = Uri.decode(gifUrl)
    Log.d("GifDetailScreen", "Decoded gif URL: $decodedUrl")

    val painter = rememberImagePainter(
        data = decodedUrl,
        imageLoader = imageLoader,
        builder = {
            crossfade(true)
        }
    )

    Column {
        Spacer(modifier = Modifier.size(10.dp))
        Row {
            Spacer(modifier = Modifier.size(10.dp))
            OutlinedButton(
                onClick = { navController.popBackStack() },
            ) {
                Icon(
                    Icons.Rounded.ArrowBack,
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.size(10.dp))
            OutlinedButton(
                onClick = { isFullSize = !isFullSize },
            ) {
                Icon(
                    Icons.Rounded.Info,
                    contentDescription = null
                )
            }
        }
        Spacer(modifier = Modifier.size(10.dp))
        if (isFullSize) {
            Image(
                painter = painter,
                contentDescription = "A gif",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .background(Color.Gray)
                    .fillMaxSize()
                    .clickable { isFullSize = !isFullSize }
                    .horizontalScroll(rememberScrollState()),
            )
        }
        else {
            Image(
                painter = painter,
                contentDescription = "A gif",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .background(Color.Gray)
                    .fillMaxWidth()
                    .clickable { isFullSize = !isFullSize }
            )
        }
        Spacer(modifier = Modifier.size(30.dp))
    }
}