package com.example.castingplaces

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.castingplaces.ui.theme.CastingPlacesTheme


@Composable
fun InfoImage(){


}

@Composable
fun CardDetailsScreen(navController: NavController, cardTitle: String) {
    val context = LocalContext.current
    val db = SQLiteHelper(context)
    val allCardsList: MutableList<Card> = remember {
        db.getAllCards()
    }

    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }


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
            CardList(navController = navController, list = allCardsList )
        }

        val kek = listOf<Card>()
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    CastingPlacesTheme {
        CardDetailsScreen(navController = rememberNavController(), cardTitle = "fgchjbm")
    }
}