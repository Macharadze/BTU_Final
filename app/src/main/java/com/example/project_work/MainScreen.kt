package com.example.project_work

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val weatherModel: WeatherModel = viewModel()

    NavHost(navController = navController, startDestination = "weather") {
        composable("weather") {
            WeatherScreen(viewModel = weatherModel)
        }
        composable("details/{date}") { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date")
            if (date != null) {
                DetailScreen(date = date, viewModel = weatherModel)
            }
        }
        composable("forecast") {
            ForecastScreen(navController = navController, viewModel = weatherModel)
        }
    }
}