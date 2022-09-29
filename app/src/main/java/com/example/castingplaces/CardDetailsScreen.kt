package com.example.castingplaces

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.castingplaces.ui.theme.CastingPlacesTheme

@Composable
fun CardDetailsScreen(navController: NavController, cardTitle: String) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top

        ) {
            TopAppBar(
                title = { Text(text = cardTitle) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()

                    /* doSomething() */

                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    CastingPlacesTheme {
        CardDetailsScreen(navController = rememberNavController(), cardTitle = "fgchjbm")
    }
}