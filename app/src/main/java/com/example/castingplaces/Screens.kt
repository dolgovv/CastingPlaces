package com.example.castingplaces

import androidx.navigation.NavArgs

sealed class Screens (val route: String){
    object MainScreen : Screens("main_screen")
    object CardDetailsScreen : Screens("card_details_screen")
    object CardInfoPickerScreen: Screens("card_info_picker_screen")

    fun withArgs(vararg args: String): String{
        return buildString {
            append(route)
            for (arg in args) {
                append("/$arg")

            }
        }
    }
}
