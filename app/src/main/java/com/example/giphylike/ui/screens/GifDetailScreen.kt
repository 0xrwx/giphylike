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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import com.example.giphylike.R
import java.util.Locale

@OptIn(ExperimentalCoilApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GifDetailScreen(gifUrl: String, gifTitle: String, navController: NavHostController, backgroundColor: Color) {
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
    Column(
        Modifier.verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.size(10.dp))
        Row {
            Spacer(modifier = Modifier.size(10.dp))

            GifDetailsScreenButton(
                Icons.Rounded.ArrowBack,
                stringResource(R.string.gif_page_move_to_home_button_description)
            ) { navController.popBackStack() }

            Spacer(modifier = Modifier.size(10.dp))

            GifDetailsScreenButton(
                Icons.Rounded.Info,
                stringResource(R.string.gif_page_info_button_description)
            ) { isFullSize = !isFullSize }
        }

        GifDetailsScreenTitle(gifTitle)

        if (isFullSize) {
            Image(
                painter = painter,
                contentDescription = stringResource(R.string.gif_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .background(backgroundColor)
                    .aspectRatio(9 / 16f)
                    .height(700.dp)
                    .clickable { isFullSize = !isFullSize }
                    .horizontalScroll(rememberScrollState())
            )
        }
        else {
            Image(
                painter = painter,
                contentDescription = stringResource(R.string.gif_description),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .background(backgroundColor)
                    .aspectRatio(9f / 12f)
                    .clickable { isFullSize = !isFullSize }
                    .verticalScroll(rememberScrollState())
            )
            Spacer(modifier = Modifier.size(8.dp))
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
}

@Composable
fun GifDetailsScreenButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
    ) {
        Icon(icon, contentDescription = contentDescription)
    }
}

@Composable
fun GifDetailsScreenTitle(gifTitle: String) {
    Text(
        text = gifTitle.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            .dropLast(3), // All gifs have GIF sign by the end. I handle it by removing
        fontSize = 26.sp,
        modifier = Modifier.padding(start = 10.dp)
    )
}