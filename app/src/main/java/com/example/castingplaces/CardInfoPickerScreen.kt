package com.example.castingplaces

import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.castingplaces.ui.theme.CastingPlacesTheme
import java.io.*
import java.util.*


var mCardName = ""
var mCardDescription = ""
var mCardDate = "PICK YOUR DATE"
var mCardLocation = "1234567890"
var mCardImage = ""

lateinit var testGlobalTempFile: File

fun compressImageFile(context: Context, file: File): Boolean {
    var result = false
    val uwu = Uri.fromFile(file)                                        //ПОЛУЧИЛ УРИ
    val inputS = context.contentResolver.openInputStream(uwu)          //ОТКРЫЛ СВЯЗЬ С ФАЙЛОМ

    if (inputS != null) {
        val inData = ByteArray(inputS.available())                //создал массив с нужным размером
        inputS.read(inData)                                       //записал картинку в массив
        val bitmap = BitmapFactory.decodeByteArray(
            inData,
            0,
            inData.size
        )                                                //сделал битмапу из массива
        inputS.close()
        val outputS: OutputStream = FileOutputStream(file)           //открыл связь с файлом записи

        val compressedBitmap: Boolean =
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputS)//сжал битмапу
        result = compressedBitmap
        outputS.flush()
        outputS.close()
    } else {
        Log.d("DATA RECEIVE FROM COMPOSABLES: ", "inputS is kinda null: $inputS")
    }

    return result
}

fun testCompressImageFile(context: Context, file: File, sizeWanted: Int): Boolean {

    var result = false
    val uwu = Uri.fromFile(file)                                        //ПОЛУЧИЛ УРИ
    val inputS = context.contentResolver.openInputStream(uwu)          //ОТКРЫЛ СВЯЗЬ С ФАЙЛОМ
    var calc: Int = 100
    var shouldItBeCompressed = true

    if (inputS != null) {
        val inData = ByteArray(inputS.available())                //создал массив с нужным размером
        inputS.read(inData)                                       //записал картинку в массив
        Log.d("DATA RECEIVE FROM COMPOSABLES: ", "inData.size is : ${inData.size}")

        if (inData.size > sizeWanted){
            val ccalc = sizeWanted.toDouble() / inData.size.toDouble() * 10
            Log.d("DATA RECEIVE FROM COMPOSABLES: ", "ccalc is: $ccalc and ${ccalc}")

            calc = if (ccalc < 10){
                10
            } else {
                ccalc.toInt()
            }
        } else { shouldItBeCompressed = false }
        Log.d("DATA RECEIVE FROM COMPOSABLES: ", "calc is: $calc")

        val bitmap = BitmapFactory.decodeByteArray(
            inData,
            0,
            inData.size
        )                                                //сделал битмапу из массива
        inputS.close()
        if (shouldItBeCompressed){
            val outputS: OutputStream = FileOutputStream(file)           //открыл связь с файлом записи
            val compressedBitmap: Boolean =
                bitmap.compress(Bitmap.CompressFormat.JPEG, calc, outputS)//сжал битмапу
            result = compressedBitmap
            outputS.flush()
            outputS.close()
        }

    } else {
        Log.d("DATA RECEIVE FROM COMPOSABLES: ", "inputS is kinda null: $inputS")
    }

    return result
}


@Composable
fun CardInfoPickerScreen(navController: NavController, cardTitle: String) {
    val context = LocalContext.current

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    var hasImage by remember { mutableStateOf(false) }
    val dialogShowVal = remember { mutableStateOf(false) }
    val directory = File(context.filesDir, "images")
    if (!directory.exists()) {
        directory.mkdirs()
    }

    val storageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri ->
        imageUri = uri
        val copyImageUri: Uri = uri
        hasImage = true
        val inputS = context.contentResolver.openInputStream(copyImageUri) //GET STREAM FROM FILE
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
            mCardImage = tempFile.toString()
            Log.d("DATA RECEIVE FROM COMPOSABLES: ", "TEMP FILE IS $tempFile")
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { cameraLauncherResult: Boolean ->
            hasImage = cameraLauncherResult
            if (hasImage) {
                testCompressImageFile(context, testGlobalTempFile, 300000)
            }
        })

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            TopAppBar(
                title = {
                    Text(text = "New place")
                },
                navigationIcon = {
                    IconButton(onClick = {

                        navController.popBackStack()
                    }) {

                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                })

            Column(
                modifier = Modifier
                    .fillMaxHeight(0.4f)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                if (hasImage) {

                    if (imageUri != null) {

                        imageUri?.let {
                            val source = ImageDecoder
                                .createSource(context.contentResolver, it)
                            bitmap.value = ImageDecoder.decodeBitmap(source)
                            hasImage = false
                            Log.d(
                                "DATA RECEIVE FROM COMPOSABLES: ",
                                "mCardBitmap is $mCardImage"
                            )
                        }
                    }
                }

                if (dialogShowVal.value) {

                    SourceDialog(
                        closeDialog = { dialogShowVal.value = false },
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

                            mCardImage = tempFile.toString()
                            /** TODO назначил картинкой для класса
                            путь к временному файлу созданному камера лаунчером*/
                            testGlobalTempFile = tempFile
                            Log.d(
                                "DATA RECEIVE FROM COMPOSABLES: ",
                                "newPhotoContentUri is $newPhotoContentUri"
                            )
                            cameraLauncher.launch(newPhotoContentUri)
                        })
                }

                TextFields(isLong = false, title = "Title")
                TextFields(isLong = true, title = "Description")
                DatePickerButton()
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = {},
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(Dp.Hairline, MaterialTheme.colors.onSurface)
                ) {

                    Text(text = "PICK A LOCATION")
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxHeight(0.5f)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween

                ) {
                    Column(
                        modifier = Modifier
                            .height(200.dp)
                            .width(200.dp)

                    ) {

                        if (bitmap.value != null) {
                            bitmap.value?.let { btm ->
                                PickedImage(bitmap = btm)
                            }
                        } else {
                            DefaultImage()
                        }
                    }

                    Column(
                        modifier = Modifier
                            .height(200.dp)
                            .width(200.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center

                    ) {
                        ButtonAddImage(
                            dialogShow = {
                                dialogShowVal.value = true
                            })
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 20.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.End
                ) {
                    AcceptNewCardButton {
                        saveTheCard(context, navController)
                    }
                }
            }
        }
    }
}

fun saveTheCard(context: Context, navController: NavController) {

    when {
        mCardName == "" -> Toast.makeText(context, "Please, name your card", Toast.LENGTH_LONG)
            .show()
        mCardDescription == "" -> Toast.makeText(
            context,
            "Please, describe your card",
            Toast.LENGTH_LONG
        ).show()
        mCardDate == "PICK YOUR DATE" -> Toast.makeText(
            context,
            "Please, add a date to your card",
            Toast.LENGTH_LONG
        )
            .show()
        mCardLocation == "" -> Toast.makeText(
            context,
            "Please, choose a location for your card",
            Toast.LENGTH_LONG
        )
            .show()
        mCardImage == "" -> Toast.makeText(
            context,
            "Please, select an image for your card",
            Toast.LENGTH_LONG
        )
            .show()

        else -> {
            val createdCard = Card(
                0,
                mCardName,
                mCardDescription,
                mCardDate,
                mCardLocation,
                mCardImage
            )
            val dbHandler = SQLiteHelper(context)

            val addPlace = dbHandler.addCard(createdCard)

            if (addPlace > 0) {
                Toast.makeText(context, "You created a ${createdCard.getName()} card",
                    Toast.LENGTH_LONG)
                    .show()
                navController.navigate(route = Screens.MainScreen.route)
            }
        }
    }
}


/** BUTTONS */

@Composable
fun ButtonAddImage(
    dialogShow: () -> Unit
) {
    TextButton(
        modifier = Modifier
            .fillMaxSize(),
        onClick = { dialogShow() }) {
        Text(
            text = "ADD IMAGE",
            fontSize = 16.sp
        )
    }
}

@Composable
fun DatePickerButton() {

    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    val pickedDate = remember {
        mutableStateOf("PICK YOUR DATE")
    }
    var buttonText by pickedDate

    val dpd = DatePickerDialog(
        context,
        DatePickerDialog.OnDateSetListener { _, slctdyear, slctdmonth, slctddayOfMonth ->

            buttonText = "$slctddayOfMonth/${slctdmonth + 1}/$slctdyear"
            mCardDate = buttonText
            Log.d(
                "DATA RECEIVE FROM COMPOSABLES: ",
                "mCardDate is $mCardDate"
            )
        },
        currentYear,
        currentMonth,
        currentDay
    )

    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        onClick = {
            dpd.show()
        },
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(Dp.Hairline, MaterialTheme.colors.onSurface)
    ) {

        Text(text = buttonText)
    }
}

@Composable
fun ButtonImageSourcePicker(
    title: String,
    onClick: () -> Unit,
    dialogShow: () -> Unit
) {

    val context = LocalContext.current

    val permLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {

            dialogShow()
            onClick()

            Log.d("ExampleScreen", "PERMISSION GRANTED")
        } else {
            Log.d("ExampleScreen", "PERMISSION DENIED")
        }
    }

    TextButton(modifier = Modifier.padding(10.dp),
        onClick = {
            if (title == "Camera") {
                if (ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    onClick()
                    dialogShow()
                } else {
                    permLauncher.launch(android.Manifest.permission.CAMERA)
                }
            } else if (title == "Gallery") {
                if (ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    onClick()
                    dialogShow()
                } else {
                    permLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }) {
        Text(
            text = title,
            fontSize = 20.sp
        )
    }
}

/** OTHER SURFACES */

@Composable
fun TextFields(isLong: Boolean, title: String) {

    var cardTitle: String by remember {
        mutableStateOf("")
    }

    var cardDescription: String by remember {
        mutableStateOf("")
    }

    if (isLong) {

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            value = cardDescription,
            onValueChange = {
                cardDescription = it
                mCardDescription = cardDescription
                Log.d(
                    "DATA RECEIVE FROM COMPOSABLES: ",
                    "mCardDescription is $mCardDescription"
                )
            },
            singleLine = false,
            label = { Text(text = title) })

    } else {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = cardTitle,
            onValueChange = {
                if (it.length <= 20) {
                    cardTitle = it
                    mCardName = cardTitle
                    Log.d(
                        "DATA RECEIVE FROM COMPOSABLES: ",
                        "mCardName is $mCardName"
                    )
                }
            },
            singleLine = true,
            label = {
                Text(text = title)
            })
    }
}

@Composable
fun SourceDialog(
    closeDialog: () -> Unit,
    runStorageLauncher: () -> Unit,
    runCameraLauncher: () -> Unit,
) {
    AlertDialog(modifier = Modifier
        .clip(RoundedCornerShape(10.dp)),
        onDismissRequest = {
            closeDialog()
        },
        title = {
            Text(text = "Choose source")
        },
        buttons = {

            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                ButtonImageSourcePicker("Gallery",
                    onClick = {
                        runStorageLauncher()
                    },
                    dialogShow = { closeDialog() }
                )

                ButtonImageSourcePicker("Camera",
                    onClick = {
                        runCameraLauncher()

                    },
                    dialogShow = { closeDialog() }
                )
            }
        })
}

@Composable
fun PickedImage(bitmap: Bitmap) {
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp)),
    )
}

@Composable
fun DefaultImage() {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_background),
        contentDescription = "default image",
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .border(
                border = BorderStroke(
                    Dp.Hairline,
                    MaterialTheme.colors.onSurface
                )
            )
    )
}

@Composable
fun AcceptNewCardButton(saveCard: () -> Unit) {

    FloatingActionButton(
        onClick = { saveCard() },
        backgroundColor = MaterialTheme.colors.surface,
    ) {
        Icon(
            Icons.Filled.Done,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp),
            contentColorFor(backgroundColor = MaterialTheme.colors.surface)
        )
    }
}


/** ======= PREVIEWS ======= */

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun FullPreview() {
    CastingPlacesTheme {
        CardInfoPickerScreen(navController = rememberNavController(), cardTitle = "you")
    }
}

//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Preview(showBackground = true)
//@Composable
//fun FieldPreview() {
//    CastingPlacesTheme {
//        SourceDialog(
//            closeDialog = { /*TODO*/ },
//            runStorageLauncher = { /*TODO*/ },
//            runCameraLauncher = { /*TODO*/ }
//        ) } }