package com.example.castingplaces

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
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
import java.io.File
import java.io.FileInputStream

@Composable
fun CardInfoScreen(id: Int, navController: NavController) {
    var isItSaved: Boolean by remember { mutableStateOf(true) }
    var showDialog: Boolean by remember { mutableStateOf(false) }
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
                        if (isItSaved) {
                            navController.popBackStack()
                        } else {
                            showDialog = true
                        }
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
            if (showDialog) {
                AlertDialog(modifier = Modifier
                    .clip(RoundedCornerShape(10.dp)),
                    onDismissRequest = { },
                    title = {
                        Text(text = "some data not saved yet")
                    },
                    buttons = {

                        Row(
                            modifier = Modifier.padding(all = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {

                            ButtonImageSourcePicker("Cancel",
                                onClick = { },
                                dialogShow = { }
                            )

                            ButtonImageSourcePicker("Save",
                                onClick = { },
                                dialogShow = { }
                            )
                        }
                    })
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                InfoMain(
                    card = currentCard,
                    isSaved = { isItSaved = it },
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun InfoMain(card: Card, isSaved: (Boolean) -> Unit, navController: NavController) {
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
        TextInfo(card = card, isSaved = { isSaved(it) }, navController = navController)
    }
}

@Composable
fun TextInfo(card: Card, isSaved: (Boolean) -> Unit, navController: NavController) {
    val context = LocalContext.current
    val dbHandler = SQLiteHelper(context)

    var newName: String by remember { mutableStateOf(card.getName()) }
    var newDescription: String by remember { mutableStateOf(card.getDescription()) }
    var newDate: String by remember { mutableStateOf(card.getDate()) }
    var newLocation: String by remember { mutableStateOf(card.getLocation()) }
    var newImage: String by remember { mutableStateOf(card.getImage()) }

    var isEditable by remember {
        mutableStateOf(false)
    }

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
            value = newName,
            onValueChange = {
                if (it != card.getName()) {
                    isSaved(false)

                    Log.d("PreventDialog", " $it = ${card.getName()}, and state is ")
                }
                newName = it
            },
            enabled = isEditable,
            label = { Text(text = "Title") },
            trailingIcon = {

                Row(
                    modifier = Modifier
                        .size(55.dp)
                        .clickable {
                            if (!isEditable) {
                                /** TODO copyToBuffer() */
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
            value = newDescription,
            onValueChange = {
                if (it != card.getName()) {
                    isSaved(false)
                }
                newDescription = it
            },
            enabled = isEditable,
            label = { Text(text = "Description") },
            trailingIcon = {

                Row(
                    modifier = Modifier
                        .size(55.dp)
                        .clickable {
                            if (!isEditable) {
                                /** TODO copyToBuffer() */
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
            onClick = { /** TODO вынести функционал дата пикера за пределы cardinfopickerscreen и заюзать тут */ },
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
            onClick = { /** TODO сделать, а затем вынести функционал локации за пределы cardinfopickerscreen и заюзать тут */ },
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(Dp.Hairline, MaterialTheme.colors.onSurface),
            enabled = isEditable
        ) {

            Text(text = card.getLocation())
        }

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .height(50.dp),
            onClick = {
                dbHandler.deleteCard(card)
                navController.popBackStack()
            },
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(Dp.Hairline, MaterialTheme.colors.onSurface)
        ) {
            Text(text = "Delete")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 10.dp)
                .padding(end = 10.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {

            FloatingActionButton(
                onClick = {
                    isEditable = !isEditable
                    if (isEditable) {

                    } else {

                        val updatedCard = Card(
                            card.getId(),
                            newName,
                            newDescription,
                            newDate,
                            newLocation,
                            newImage
                        )
                        isSaved(true)
                        dbHandler.updateCard(updatedCard)
                    }
                },
                backgroundColor = MaterialTheme.colors.surface,
            ) {
                if (isEditable) {
                    Icon(
                        Icons.Filled.Done,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp),
                        contentColorFor(backgroundColor = MaterialTheme.colors.surface)
                    )
                } else {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp),
                        contentColorFor(backgroundColor = MaterialTheme.colors.surface)
                    )
                }
            }
        }
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
    val context = LocalContext.current
    val sourceDialogShow by remember {
        mutableStateOf(false) }

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
        if (sourceDialogShow) {
            SourceDialog(
                closeDialog = { /*TODO*/ },
                runStorageLauncher = {
                                     
                                     },
                runCameraLauncher = {

                    Log.d("asasd", "dsasd")}
            )

        }
        IconButton(onClick = {
            /** TODO

            вынести функционал сурс пикера и лаунчеров
            за пределы cardinfopickerscreen и заюзать тут */
        }) {

            Icon(Icons.Filled.Edit, contentDescription = "null")
        }
        IconButton(onClick = { /*TODO картинка на полный экран */ }) {
            Icon(Icons.Filled.Info, contentDescription = "null")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CardInfoScreen(3, rememberNavController())
}