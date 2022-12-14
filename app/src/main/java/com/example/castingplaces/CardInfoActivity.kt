package com.example.castingplaces

import android.R.attr.label
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import androidx.compose.ui.ExperimentalComposeUiApi
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.FileProvider
import androidx.core.content.getSystemService
import androidx.navigation.NavController
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

class CardInfoAct : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        if (!Places.isInitialized()) {
            Places.initialize(
                this@CardInfoAct,
                resources.getString(R.string.casting_places_api_key)
            )
        }
        try {
            // These are the list of fields which we required is passed
            val fields = listOf(
                Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG,
                Place.Field.ADDRESS
            )
            // Start the autocomplete intent with a unique request code.
            val mapIntent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            ).build(this@CardInfoAct)
            startActivityForResult(mapIntent, MapActivity.MAP_REQUEST_CODE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == MapActivity.MAP_REQUEST_CODE) {

                val place: Place = Autocomplete.getPlaceFromIntent(data!!)

                mLocation     = place.address as String
                mCardLocation.value = place.address as String
                Log.d("map opener lol", "$mCardLocation.value")
                finish()
            }
            // END
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("map opener lol", "Cancelled")
            finish()
        }
    }
}

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
                title = { Text(text = "Edit your card") },
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

                if (showDialog) {
                    PreventDialog(closeDialog = { showDialog = false })
                }

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
    /** ?????????????????? ?????????????? ??????????. ???????????????????? ?????? ???????????????????? ??????????. ???????????? ???????? ?? ?????????? */
    id: Int,
    isSaved: (Boolean) -> Unit,
    navController: NavController,
) {
    val context = LocalContext.current
    val mapIntent = Intent(context, MapActivity::class.java)
    val dbHandler = SQLiteHelper(context)

    val currentCard       : Card by remember { mutableStateOf(dbHandler.getCard(id)) }
    val cardID             : Int by remember { mutableStateOf(currentCard.getId()) }
    var newName         : String by remember { mutableStateOf(currentCard.getName()) }
    var newDescription  : String by remember { mutableStateOf(currentCard.getDescription()) }
    var newDate         : String by remember { mutableStateOf(currentCard.getDate()) }
    var newLocation     : String by remember { mutableStateOf(currentCard.getLocation()) }
    var newImage        : String by remember { mutableStateOf(currentCard.getImage()) }

    var isEditable           by remember { mutableStateOf(false) }
    var isImageChanged       by remember { mutableStateOf(false) } //???????????????? ???? ???????????????????????? ????????????????

    var bitmap               by remember { mutableStateOf<Bitmap?>(null) }

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
        inputS.close()
        newImage = newCardImage
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (bitmap != null) {
            bitmap?.let {
                CardInfoImage(
                    bitmap = it,
                    card = currentCard,
                    isEditable = isEditable,
                    isImageChanged = { isImageChanged = it })
            }
        } else { DefaultImage() }

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
                if (it.length <= 15) {
                    newName = it
                } else { newName = it.dropLast(it.length - 15) }
            },
            enabled = isEditable,
            label = { Text(text = "Title") },
            singleLine = true,
            trailingIcon = {

                Row(
                    modifier = Modifier
                        .size(55.dp)
                        .clickable { copyToBuffer(context = context, text = newName) },
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
                                copyToBuffer(context = context, text = newDescription)
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

        DatePickerInfoButton(
            isSaved = { isSaved(it) },
            isEditable = isEditable,
            newDate = {newDate = it},
            currentDate = newDate )

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .height(50.dp),
            onClick = { /** TODO ??????????????, ?? ?????????? ?????????????? ???????????????????? ?????????????? ???? ?????????????? cardinfopickerscreen ?? ?????????????? ?????? */
                context.startActivity(mapIntent)
                      },
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(Dp.Hairline, MaterialTheme.colors.onSurface),
            enabled = isEditable
        ) {
            if(mCardLocation.value == currentCard.getLocation()){
                Text(text = currentCard.getLocation())
            } else {
                Text(text = mCardLocation.value)
            }
        }

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
            border = BorderStroke(Dp.Hairline, color = Color.Red)
        ) {
            Text(text = "Delete",
            color = Color.Red)
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
                            context =        context,
                            card =           currentCard,
                            id =             cardID,
                            newName =        newName,
                            newDescription = newDescription,
                            newDate =        newDate,
                            newLocation =    newLocation,
                            newImage =       newImage )

                            isSaved(true)
                   }
                },
                backgroundColor = MaterialTheme.colors.surface,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 15.dp,
                    pressedElevation = 25.dp
                )
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

fun copyToBuffer(context: Context, text: String) {
   // val clip: ClipboardManager = Context.CLIPBOARD_SERVICE
    val string = text.toString()

    val clipboard = getSystemService(context, ClipboardManager::class.java)
    var clip = ClipData.newPlainText(text, string)
    clipboard!!.setPrimaryClip(clip)
    Toast.makeText(context, "Text copied to your clipboard", Toast.LENGTH_SHORT).show()
    Log.d("copy text", "${clipboard.primaryClip}")

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CardInfoImage(
    bitmap: Bitmap,
    card: Card,
    isEditable: Boolean,
    isImageChanged: (Boolean) -> Unit
) {
    var showFullImage by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .height(250.dp)
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.BottomEnd
    ) {

        if (showFullImage) {
            Dialog(
                onDismissRequest = { showFullImage = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "kek",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { showFullImage = false }
                        .padding(10.dp)
                        .padding(vertical = 60.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }
        }

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
            if (isEditable) { // ???????? ?????????????????????? ???????????????????????????? ???????????? ?????????? isSaved state = false
                OpenAndEditImage(card = card, isImageChanged = { isImageChanged(it) })
            } else {
                IconButton(onClick = {
                    showFullImage = true

                }) {
                    Icon(Icons.Filled.Info, contentDescription = "null")
                }
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

    val storageInfoLauncher = rememberLauncherForActivityResult(
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
            isImageChanged(true)
            Log.d("DATA RECEIVE FROM COMPOSABLES: ", "TEMP FILE IS $tempFile")

        }
    }

    val cameraInfoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { cameraLauncherResult: Boolean ->
            hasImage = cameraLauncherResult
            if (hasImage) {
                testCompressImageFile(context, testGlobalTempFile, 300000)
                isImageChanged(true)
            }
        })


    if (sourceDialogShow) {
        SourceDialog(
            closeDialog = { sourceDialogShow = false },
            runStorageLauncher = { storageInfoLauncher.launch("image/*") },
            runCameraLauncher = {
                val tempFile: File = File.createTempFile(
                    "temp_file_selected_picture",
                    ".jpg", directory )

                val newPhotoContentUri = FileProvider.getUriForFile(
                    context,
                    BuildConfig.APPLICATION_ID + ".fileprovider",
                    tempFile )

                imageUri = newPhotoContentUri

                testCompressImageFile(context, tempFile, 300000)
                newCardImage = tempFile.toString()

                testGlobalTempFile = tempFile
                //isImageChanged(true)
                Log.d(
                    "DATA RECEIVE FROM COMPOSABLES: ",
                    "newPhotoContentUri is $newPhotoContentUri"
                )
                cameraInfoLauncher.launch(newPhotoContentUri)
            }
        )
    }

    IconButton(onClick = {
        sourceDialogShow = true
        /** TODO

        ?????????????? ???????????????????? ???????? ???????????? ?? ??????????????????
        ???? ?????????????? cardinfopickerscreen ?? ?????????????? ?????? */
    }) {

        Icon(Icons.Filled.Edit, contentDescription = "null")
    }
}

@Composable
fun PreventDialog(
    closeDialog: () -> Unit,
    //saveCardFromDialog: () -> Unit
) {
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

@Composable
fun DatePickerInfoButton(
    currentDate: String,
    isSaved: (Boolean) -> Unit,
    isEditable: Boolean,
    newDate: (String) -> Unit
) {

    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    val pickedDate = remember { mutableStateOf(currentDate) }
    var buttonText: String = pickedDate.value

    val dpd = DatePickerDialog(
        context,
        DatePickerDialog.OnDateSetListener { _, slctdyear, slctdmonth, slctddayOfMonth ->

            buttonText = "$slctddayOfMonth/${slctdmonth + 1}/$slctdyear"
            pickedDate.value = "$slctddayOfMonth/${slctdmonth + 1}/$slctdyear"
            Log.d(
                "datepicker problem: ",
                "pickedDate = ${pickedDate.value}, ${currentDate}")
        },
        currentYear,
        currentMonth,
        currentDay
    )
    if (currentDate != pickedDate.value){
        isSaved(false)
    newDate(buttonText) }

    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .height(50.dp),
        onClick = {
            dpd.show()
        },
        shape = RoundedCornerShape(10.dp),
        enabled = isEditable,
        border = BorderStroke(Dp.Hairline, MaterialTheme.colors.onSurface)
    ) {
        Text(text = buttonText)
    }
}


fun saveCard(
    context: Context, card: Card,id: Int, newName: String, newDescription: String, newDate: String,
    newLocation: String, newImage: String
) {

    val dbHandler = SQLiteHelper(context)

    val updatedCard = Card(
        id,
        newName,
        newDescription,
        newDate,
        newLocation,
        newImage
    )

    dbHandler.updateCard(updatedCard)
}