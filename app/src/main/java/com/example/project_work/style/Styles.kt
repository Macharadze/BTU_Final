package com.example.project_work.style

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val boxStyle = Modifier
    .fillMaxSize()


val weatherStyle = Modifier
    .fillMaxSize()

fun formatTime(rawTime: String): String {
    return rawTime.split("T").getOrNull(1) ?: "Invalid time"
}

