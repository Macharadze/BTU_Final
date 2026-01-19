package com.example.project_work.images

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
@Composable
fun CoilUrlImage(
    url: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    @DrawableRes placeholderResId: Int? = null,
    @DrawableRes errorResId: Int? = null,
) {
    // Prefer AsyncImage when it's available.
    // If your IDE can't resolve it but Gradle can, sync/rebuild should fix it.
    // If only coil-compose-base is on the classpath, SubcomposeAsyncImage will still work.
    runCatching {
        AsyncImage(
            model = url,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
            placeholder = placeholderResId?.let { painterResource(it) },
            error = errorResId?.let { painterResource(it) },
        )
    }.getOrElse {
        SubcomposeAsyncImage(
            model = url,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
        )
    }
}

/**
 * Loads a local drawable resource through Coil.
 *
 * Handy when you already have an icon id, e.g. from [getWeatherIcon].
 */
@Composable
fun CoilDrawableImage(
    @DrawableRes drawableResId: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Fit,
) {
    runCatching {
        AsyncImage(
            model = drawableResId,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
        )
    }.getOrElse {
        SubcomposeAsyncImage(
            model = drawableResId,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CoilDrawableImagePreview() {
    // Demonstrates loading a local drawable resource.
    // Uses an existing icon from getWeatherIcon (0 == clear).
    Box(modifier = Modifier.size(96.dp), contentAlignment = Alignment.Center) {
        CoilDrawableImage(
            drawableResId = getWeatherIcon(0),
            modifier = Modifier.size(64.dp),
            contentDescription = "Weather icon"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CoilUrlImagePreview() {
    // Demonstrates loading from a URL.
    // Note: Previews may not fetch network images depending on IDE/preview settings.
    Box(modifier = Modifier.size(96.dp), contentAlignment = Alignment.Center) {
        CoilUrlImage(
            url = "https://picsum.photos/200",
            modifier = Modifier.size(64.dp),
            contentDescription = "Random image"
        )
    }
}
