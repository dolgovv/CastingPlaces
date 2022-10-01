package com.example.castingplaces

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.MainScreen.route

    ) {
        composable(route = Screens.MainScreen.route) {
            HomeScreen(navController)
        }

        composable(route = Screens.CardDetailsScreen.route) {
            CardDetailsScreen(navController = navController, "fromNavigation")
        }
        composable(route = Screens.CardInfoPickerScreen.route) {
            CardInfoPickerScreen(navController, "fromNavigation")
        }
    }
}