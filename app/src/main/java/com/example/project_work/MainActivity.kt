package com.example.project_work

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.project_work.menu.BottomNavBar
import com.example.project_work.menu.Screen
import com.example.project_work.ui.theme.ProjectworkTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectworkTheme(dynamicColor = false) {
                val navController = rememberNavController()

                // Keep a single WeatherModel instance for the whole navigation graph
                val weatherModel: WeatherModel = viewModel()
                Scaffold(
                    bottomBar = { BottomNavBar(navController) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Home.route) {
                            WeatherScreen(viewModel = weatherModel)
                        }

                        composable(Screen.Forecast.route) {
                            ForecastScreen(navController = navController, viewModel = weatherModel)
                        }

                        composable("details/{date}") { backStackEntry ->
                            val dateArg = backStackEntry.arguments?.getString("date").orEmpty()
                            DetailScreen(date = dateArg, viewModel = weatherModel)
                        }
                    }
                }
            }
        }
    }

}
