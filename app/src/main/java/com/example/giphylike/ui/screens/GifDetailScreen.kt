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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import com.example.giphylike.R

@OptIn(ExperimentalCoilApi::class, ExperimentalMaterial3Api::class)
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
                    contentDescription = stringResource(R.string.gif_page_move_to_home_button_description)
                )
            }
            Spacer(modifier = Modifier.size(10.dp))
            OutlinedButton(
                onClick = { isFullSize = !isFullSize },
            ) {
                Icon(
                    Icons.Rounded.Info,
                    contentDescription = stringResource(R.string.gif_page_info_button_description)
                )
            }
        }
        Spacer(modifier = Modifier.size(10.dp))
        if (isFullSize) {
            Image(
                painter = painter,
                contentDescription = stringResource(R.string.gif_description),
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
                contentDescription = stringResource(R.string.gif_description),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .background(Color.Gray)
                    .fillMaxWidth()
                    .clickable { isFullSize = !isFullSize }
            )
        }
        Spacer(modifier = Modifier.size(20.dp))
        Text(text = "Url: ", Modifier.padding(start = 15.dp))
        TextField(
            value = decodedUrl,
            onValueChange = {},
            readOnly = true,
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
    }
}