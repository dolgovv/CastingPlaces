package com.example.castingplaces

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.MainScreen.route

    ) {
        composable(route = Screens.MainScreen.route,
            arguments = listOf(navArgument("cardID") { type = NavType.StringType })) { back ->


            HomeScreen(navController = navController)
        }
        composable(route = Screens.CardInfoActivity.route + "/{cardID}",
        arguments = listOf(navArgument("cardID") {type = NavType.IntType})) { back ->

            CardInfoScreen(navController = navController, id = back.arguments!!.getInt("cardID"))
        }
        composable(route = Screens.CardInfoPickerScreen.route) {
            CardInfoPickerScreen(navController = navController, "fromNavigation")
        }
    }
}