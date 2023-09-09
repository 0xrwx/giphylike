package com.example.giphylike

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import com.example.giphylike.ui.theme.GiphyLikeTheme
import com.example.giphylike.model.DataObject
import com.example.giphylike.ui.screens.GifDetailScreen
import com.example.giphylike.ui.screens.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GiphyLikeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var searchData by remember { mutableStateOf("Anime") }
                    var ratingInfo by remember { mutableStateOf("g") }
                    val navController = rememberNavController()

                    val isDarkTheme = isSystemInDarkTheme()
                    val backgroundColor = if (isDarkTheme) {
                        colorResource(R.color.graphite)
                    } else {
                        colorResource(R.color.softLavender)
                    }

                    NavHost(navController = navController, startDestination = GifsScreen.Overview.route) {
                        composable(GifsScreen.Overview.route) {
                            HomeScreen(
                                navController,
                                searchData,
                                ratingInfo,
                                onSearchDataChange = { newSearchData ->
                                    searchData = newSearchData
                                },
                                onRatingInfoChange = { newRatingInfo ->
                                    ratingInfo = newRatingInfo
                                },
                                backgroundColor
                            )
                        }
                        composable(GifsScreen.Detail.route) { backStackEntry ->
                            val gifUrl = backStackEntry.arguments?.getString("gifUrl")
                            if (gifUrl != null) {
                                GifDetailScreen(gifUrl, navController, backgroundColor)
                            }
                        }
                    }
                }
            }
        }
    }
}

const val BASE_URL = "https://api.giphy.com/v1/"

enum class GifsScreen(val route: String) {
    Overview("overview"),
    Detail("detail/{gifUrl}")
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun GifItem(gifs: DataObject, navController: NavHostController, backgroundColor: Color) {
    val context = LocalContext.current

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .componentRegistry { add(GifDecoder()) }
            .build()
    }

    val painter = rememberImagePainter(
        data = gifs.images.source_smalest.url,
        imageLoader = imageLoader,
        builder = {
            crossfade(true)
        }
    )

//    val isDarkTheme = isSystemInDarkTheme()
//    val backgroundColor = if (isDarkTheme) {
//        colorResource(R.color.graphite)
//    } else {
//        colorResource(R.color.softLavender)
//    }
    Image(
        painter = painter,
        contentDescription = stringResource(R.string.gif_description),
        modifier = Modifier
            .size(200.dp)
            .padding(2.dp)
            .background(backgroundColor)
            .clickable {
                val routeToNavigate =
                    GifsScreen.Detail.route.replace("{gifUrl}", Uri.encode(gifs.images.source_smalest.url))
                Log.d("NavigationAction", "Navigating to: $routeToNavigate")
                navController.navigate(routeToNavigate)
            }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GiphyLikeTheme {
//        Greeting("Android")
    }
}