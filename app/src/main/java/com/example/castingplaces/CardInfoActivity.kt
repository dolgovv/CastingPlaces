package com.example.castingplaces

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.castingplaces.ui.theme.CastingPlacesTheme
import java.io.FileInputStream

@Composable
fun CardInfoScreen(id: Int, navController: NavController) {
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
                    .fillMaxSize()
                    .padding(10.dp)
                ,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InfoMain(card = currentCard)
            } } } }

@Composable
fun InfoMain(card: Card) {
    val inputS = FileInputStream(card.getImage())
    val inData = ByteArray(inputS.available())
    inputS.read(inData)
    val bitmap = BitmapFactory.decodeByteArray(
        inData,
        0,
        inData.size
    )
    inputS.close()


    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardInfoImage(bitmap)
        TextInfo(card = card)
    }
}

@Composable
fun CardInfoImage(bitmap: Bitmap) {
    Box(
        modifier = Modifier
            .height(250.dp)
            .fillMaxWidth()
            .padding(vertical = 10.dp),
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
fun OpenAndEditImage() {
    Row(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    0F to Color.Transparent,
                    0.9F to MaterialTheme.colors.background.copy(alpha = 1F)
                )
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End

    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Filled.Edit, contentDescription = "null")
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Filled.Info, contentDescription = "null")
        }
    }
}

@Composable
fun TextInfo(card: Card) {
    val context = LocalContext.current

    var isEditable by remember {
        mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            value = card.getName(),
            onValueChange = { /** TODO onValueChange */ it },
            enabled = isEditable,
            label = { Text(text = "Title") },
            trailingIcon = {
                Row(
                    modifier = Modifier
                        .size(55.dp)
                        .clickable {
                            if (!isEditable) {
                                /** TODO copyToBuffer() */
                                Toast
                                    .makeText(context, "TextField Clicked", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Filled.Share, contentDescription = "copy title",
                        modifier = Modifier
                    )
                }
            })

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            value = card.getDescription(),
            onValueChange = { it },
            enabled = isEditable,
            label = { Text(text = "Description") },
            trailingIcon = {
                Row(
                    modifier = Modifier
                        .size(55.dp)
                        .clickable {
                            if (!isEditable) {
                                /** TODO copyToBuffer() */
                                Toast
                                    .makeText(context, "TextField Clicked", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Filled.Share, contentDescription = "copy title",
                        modifier = Modifier
                    )
                }
            })

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .height(50.dp),
            onClick = { },
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(Dp.Hairline, MaterialTheme.colors.onSurface),
            enabled = isEditable
        ) {

            Text(text = card.getDate())
        }

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .height(50.dp),
            onClick = { },
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(Dp.Hairline, MaterialTheme.colors.onSurface),
            enabled = isEditable
        ) {

            Text(text = card.getLocation())
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp)
            .padding(end = 10.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {

            FloatingActionButton(
                onClick = { isEditable = !isEditable
                          if (isEditable){
                              Toast.makeText(context, "isEditable = true", Toast.LENGTH_SHORT).show()
                          }else{
                              Toast.makeText(context, "isEditable = false", Toast.LENGTH_SHORT).show()
                          }},
                backgroundColor = MaterialTheme.colors.surface,
            ) {
                if (isEditable){
                    Icon(
                        Icons.Filled.Done,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp),
                        contentColorFor(backgroundColor = MaterialTheme.colors.surface))
                } else {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp),
                        contentColorFor(backgroundColor = MaterialTheme.colors.surface))
                } } } } }

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CastingPlacesTheme {
        CardInfoScreen(3, rememberNavController())
    }
}