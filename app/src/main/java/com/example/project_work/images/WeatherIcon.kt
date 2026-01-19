package com.example.project_work.images

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale

@Composable
fun WeatherIcon(
    @DrawableRes iconId: Int?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val safeIconId = iconId?.takeIf { it != 0 } ?: getWeatherIcon(0)

    CoilDrawableImage(
        drawableResId = safeIconId,
        modifier = modifier,
        contentDescription = contentDescription,
        contentScale = contentScale,
    )
}

