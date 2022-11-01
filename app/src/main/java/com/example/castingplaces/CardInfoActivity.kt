package com.example.castingplaces

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.castingplaces.ui.theme.CastingPlacesTheme
import java.io.FileInputStream

@Composable
fun CardInfoScreen(id: Int, navController: NavController){
    val context = LocalContext.current
    val db = SQLiteHelper(context)
    val allCardsList: MutableList<Card> = remember {
        db.getAllCards()
    }
    val currentCard: Card = allCardsList[id - 1]

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
                title = { Text(text = id.toString()) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )

            Column(
                modifier = Modifier
                .fillMaxSize()) {
                InfoMain(card = currentCard)

            }
        }
    }
}

@Composable
fun InfoMain(card: Card){
    val context = LocalContext.current
    val inputS = FileInputStream(card.getImage())
    val inData = ByteArray(inputS.available())
    inputS.read(inData)
    val bitmap = BitmapFactory.decodeByteArray(inData,
        0,
        inData.size)
    inputS.close()


    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardInfoImage(bitmap)
    }
}

@Composable
fun CardInfoImage(bitmap: Bitmap) {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.Black),
        startY = bitmap.height.toFloat(),
        endY = bitmap.height.toFloat()
    )
    val gradientGreenRed = Brush.horizontalGradient(0f to Color.Green, 1000f to Color.Red)
    Box(modifier = Modifier
        .height(250.dp)
        .fillMaxWidth()
        .padding(10.dp),

        contentAlignment = Alignment.BottomEnd
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(10.dp)),
        )

        OpenAndEditImage()
    }
}

@Composable
fun OpenAndEditImage(){
    Row(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .background(Brush.verticalGradient(
                0F to Color.Transparent,
                0.9F to MaterialTheme.colors.background.copy(alpha = 1F)
            )),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End

    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Filled.Edit, contentDescription = "null" )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Filled.Info, contentDescription = "null" )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CastingPlacesTheme {
        CardInfoScreen(2, rememberNavController())
    }
}