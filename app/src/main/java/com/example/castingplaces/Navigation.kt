package com.example.castingplaces

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation() {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.MainScreen.route){
        composable(route = Screens.MainScreen.route){

            MainScreen(navController = navController)
        }
        composable(route = Screens.CardDetailsScreen.withArgs(),
        arguments = listOf(
            navArgument("card_title"){
                type = NavType.StringType
                defaultValue = "Unknown card"
                nullable = true
            }
        )){entry ->
            CardDetailsScreen(cardTitle = entry.arguments?.getString("card_title"))

        }
    }
}