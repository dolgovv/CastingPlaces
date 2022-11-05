package com.example.castingplaces

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.castingplaces.ui.theme.CastingPlacesTheme
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream

var newCardImage: String = ""

@Composable
fun CardInfoScreen(id: Int, navController: NavController) {
    var isItSaved: Boolean by remember { mutableStateOf(true) }
    var showDialog: Boolean by remember { mutableStateOf(false) }

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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (showDialog){ PreventDialog(closeDialog = { showDialog = false }) }

                InfoMain(
                    id = id,
                    isSaved = { isItSaved = it },
                    navController = navController,
                )
            }
        }
    }
}

@Composable
fun InfoMain(
    /** экземпляр текущей карты. переменные для объявления новой. апдейт инфы о карте */
    id: Int,
    isSaved: (Boolean) -> Unit,
    navController: NavController,
) {
    val context = LocalContext.current

    val dbHandler = SQLiteHelper(context)
    val allCardsList: MutableList<Card> = remember { dbHandler.getAllCards() }
    val currentCard: Card by remember { mutableStateOf<Card>(allCardsList[id - 1]) }

    var newName: String by remember { mutableStateOf(currentCard.getName()) }
    var newDescription: String by remember { mutableStateOf(currentCard.getDescription()) }
    var newDate: String by remember { mutableStateOf(currentCard.getDate()) }
    var newLocation: String by remember { mutableStateOf(currentCard.getLocation()) }
    var newImage: String by remember { mutableStateOf(currentCard.getImage()) }

    var isEditable by remember { mutableStateOf(false) }
    var isImageChanged by remember { mutableStateOf(false) }

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    /** сохранено - ссылка из card.getImage()
     * не сохранено - ссылка из updatedImage мб глоабльной переменной
     * ссылка - стримы(
     * */

    if (!isImageChanged) {
        val inputS = FileInputStream(currentCard.getImage())
        val inData = ByteArray(inputS.available())
        inputS.read(inData)
        bitmap = BitmapFactory.decodeByteArray(inData, 0, inData.size)
        inputS.close()
        Log.d("updated bitmap", "yok")
    } else {
        val inputS = FileInputStream(newCardImage)
        val inData = ByteArray(inputS.available())
        inputS.read(inData)
        bitmap = BitmapFactory.decodeByteArray(
            inData, 0, inData.size
        )

        Log.d("updated bitmap", "evet")
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (isImageChanged) {
            isSaved(false)
        }
        bitmap?.let {
            CardInfoImage(
                bitmap = it,
                card = currentCard,
                isEditable = isEditable,
                isImageChanged = { isImageChanged = it })
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            value = newName,
            onValueChange = {
                if (it != currentCard.getName()) {
                    isSaved(false)

                    Log.d("PreventDialog", " $it = ${currentCard.getName()}, and state is ")
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
                if (it != currentCard.getName()) {
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
            Text(text = currentCard.getDate())
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
        ) { Text(text = currentCard.getLocation()) }

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .height(50.dp),
            onClick = {
                dbHandler.deleteCard(currentCard)
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
                    if (!isEditable) {
                        saveCard(
                            context = context,
                            card = currentCard,
                            newName = newName,
                            newDescription = newDescription,
                            newDate = newDate,
                            newLocation = newLocation,
                            newImage = newImage
                        )

                        isSaved(true)
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
fun CardInfoImage(
    bitmap: Bitmap,
    card: Card,
    isEditable: Boolean,
    isImageChanged: (Boolean) -> Unit
) {

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
            if (isEditable) { // дает возможность редактирования только когда isSaved state = false
                OpenAndEditImage(card = card, isImageChanged = { isImageChanged(it) })
            }
        }
    }
}

@Composable
fun OpenAndEditImage(card: Card, isImageChanged: (Boolean) -> Unit) {
    val context = LocalContext.current
    val db = SQLiteHelper(context)
    var sourceDialogShow by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var hasImage by remember { mutableStateOf(false) }
    val directory = File(context.filesDir, "images")
    if (!directory.exists()) {
        directory.mkdirs()
    }

    val storageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri ->
        imageUri = uri
        hasImage = true
        val inputS = context.contentResolver.openInputStream(uri) //GET STREAM FROM FILE
        inputS?.let {

            val byteArray = ByteArray(it.available())
            it.read(byteArray)                                  //GET DATA FROM STREAM TO AN ARRAY
            /** NOW THE DATA FROM THE CHOSEN FILE SHOULD BE WRITTEN AT THE $byteArray */

            val tempFile: File = File.createTempFile(
                "temp_file_selected_picture",
                ".jpg",
                directory
            )
            val outS: OutputStream = FileOutputStream(tempFile) //OPEN STREAM TO THE TEMPFILE
            outS.write(byteArray)
            /** NOW THE DATA FROM THE $byteArray SHOULD BE WRITTEN AT THE $tempFile */
            outS.flush()
            outS.close()
            inputS.close()

            testCompressImageFile(context, tempFile, 300000)
            newCardImage = tempFile.toString()
//            val updatedCard = Card(
//                card.getId(), card.getName(), card.getDescription(),
//                card.getDate(), card.getLocation(), tempFile.toString() )
//            db.updateCard(updatedCard)
            isImageChanged(true)
            Log.d("DATA RECEIVE FROM COMPOSABLES: ", "TEMP FILE IS $tempFile")

        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { cameraLauncherResult: Boolean ->
            hasImage = cameraLauncherResult
            if (hasImage) {
                compressImageFile(context, testGlobalTempFile)

            }
        })


    if (sourceDialogShow) {
        SourceDialog(
            closeDialog = { sourceDialogShow = false },
            runStorageLauncher = { storageLauncher.launch("image/*") },
            runCameraLauncher = {
                val tempFile: File = File.createTempFile(
                    "temp_file_selected_picture",
                    ".jpg", directory
                )

                val newPhotoContentUri = FileProvider.getUriForFile(
                    context,
                    BuildConfig.APPLICATION_ID + ".fileprovider",
                    tempFile
                )

                imageUri = newPhotoContentUri

                newCardImage = tempFile.toString()

                testGlobalTempFile = tempFile
                Log.d(
                    "DATA RECEIVE FROM COMPOSABLES: ",
                    "newPhotoContentUri is $newPhotoContentUri"
                )
                cameraLauncher.launch(newPhotoContentUri)
            }
        )
    }

    IconButton(onClick = {
        sourceDialogShow = true
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

@Composable
fun PreventDialog(closeDialog: () -> Unit,
                  //saveCardFromDialog: () -> Unit
){

    AlertDialog(modifier = Modifier
        .clip(RoundedCornerShape(10.dp)),
        onDismissRequest = { closeDialog() },
        title = {
            Text(text = "some data not saved yet")
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                TextButton(onClick = { closeDialog() }) {
                    Text(text = "Cancel", fontSize = 20.sp)
                }
            }
        })
}

fun saveCard(
    context: Context, card: Card, newName: String, newDescription: String, newDate: String,
    newLocation: String, newImage: String
) {

    val dbHandler = SQLiteHelper(context)

    val updatedCard = Card(
        card.getId(),
        newName,
        newDescription,
        newDate,
        newLocation,
        newImage
    )

    dbHandler.updateCard(updatedCard)
}