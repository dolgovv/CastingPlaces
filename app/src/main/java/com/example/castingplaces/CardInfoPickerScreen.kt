package com.example.castingplaces

import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
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
import androidx.loader.content.CursorLoader
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.castingplaces.ui.theme.CastingPlacesTheme
import java.io.*
import java.util.*


var mCardName = ""
var mCardDescription = ""
var mCardDate = ""
var mCardLocation = "1234567890"
var mCardImage = ""

lateinit var testGlobalTempFile: File

//fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
//    var cursor: Cursor? = null
//
//        val proj = arrayOf(MediaStore.Images.Media.DATA)
//        cursor = context.contentResolver.query(contentUri, proj, null, null, null)
//        cursor!!.moveToFirst()
//        val columnIndex = cursor.getColumnIndex(proj[0])
//
//        cursor.getString(columnIndex)
//
//    return cursor.getString(columnIndex)
//}

private fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
    val proj = arrayOf(MediaStore.Images.Media.DATA)
    val loader = CursorLoader(context, contentUri, proj, null, null, null)
    val cursor: Cursor? = loader.loadInBackground()
    val column_index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    cursor?.moveToFirst()
    val result = column_index?.let { cursor.getString(it) }
    cursor?.close()
    return result
}

private fun compressImageFile(context: Context, file: File): Boolean {
    Log.d("DATA RECEIVE FROM COMPOSABLES: ", "KEK IS $file")
    var result = false
    val uwu = Uri.fromFile(file)                                        //ПОЛУЧИЛ УРИ
    val inputS = context.contentResolver.openInputStream(uwu)          //ОТКРЫЛ СВЯЗЬ С ФАЙЛОМ

    if (inputS != null){
        val inData = ByteArray(inputS.available())                     //создал массив с нужным размером
        inputS.read(inData)                                             //записал картинку в массив
        val bitmap = BitmapFactory.decodeByteArray(inData,
            0,
            inData.size)                                                //сделал битмапу из массива

        val outputS: OutputStream = FileOutputStream(file)           //открыл связь с файлом записи

        val qooqoo: Boolean = bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputS)//сжал битмапу
        result = qooqoo
        //outputS.write(inData)
        //перегнал битмапу в массив
        //записал массив в файл
        outputS.flush()
        outputS.close()
    } else { Log.d("DATA RECEIVE FROM COMPOSABLES: ", "inputS is kinda null: $inputS") }


//    inputS?.let {
//        val inData = ByteArray(it.available())                     //создал массив с нужным размером
//        it.read(inData)                                             //записал картинку в массив
//        val bitmap = BitmapFactory.decodeByteArray(inData,
//            0,
//            inData.size)                                                //сделал битмапу из массива
//
//        val outputS: OutputStream = FileOutputStream(file)           //открыл связь с файлом записи
//        outputS.write(inData)
//        val qooqoo: Boolean = bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputS)//сжал битмапу
//        result = qooqoo
//        //перегнал битмапу в массив
//        //записал массив в файл
//        outputS.flush()
//        outputS.close()
//    }

return result
}

private fun compressPhotoFile(context: Context, uri: Uri): Boolean {
    Log.d("DATA RECEIVE FROM COMPOSABLES: ", "KEK IS $uri")
    var result = false
    //ПОЛУЧИЛ УРИ
    val inputS = context.contentResolver.openInputStream(uri)          //ОТКРЫЛ СВЯЗЬ С ФАЙЛОМ

    if (inputS != null){
        val inData = ByteArray(inputS.available())                     //создал массив с нужным размером
        inputS.read(inData)                                             //записал картинку в массив
        val bitmap = BitmapFactory.decodeByteArray(inData,
            0,
            inData.size)                                                //сделал битмапу из массива

        val outputS: OutputStream? = context.contentResolver.openOutputStream(uri) //открыл связь с файлом записи
        if (outputS != null) {
            val qooqoo: Boolean =
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputS)//сжал битмапу
            result = qooqoo
            outputS.flush()
            outputS.close()
        }
        //outputS.write(inData)
        //перегнал битмапу в массив
        //записал массив в файл


    } else { Log.d("DATA RECEIVE FROM COMPOSABLES: ", "inputS is kinda null: $inputS") }


//    inputS?.let {
//        val inData = ByteArray(it.available())                     //создал массив с нужным размером
//        it.read(inData)                                             //записал картинку в массив
//        val bitmap = BitmapFactory.decodeByteArray(inData,
//            0,
//            inData.size)                                                //сделал битмапу из массива
//
//        val outputS: OutputStream = FileOutputStream(file)           //открыл связь с файлом записи
//        outputS.write(inData)
//        val qooqoo: Boolean = bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputS)//сжал битмапу
//        result = qooqoo
//        //перегнал битмапу в массив
//        //записал массив в файл
//        outputS.flush()
//        outputS.close()
//    }

    return result
}

//fun compressImage(image: Bitmap): Bitmap? {
//    val baos = ByteArrayOutputStream()
//    image.compress(Bitmap.CompressFormat.JPEG, 100, baos) // 100baos
//    var options = 100
//    while (baos.toByteArray().size / 1024 > 100) { // 100kb,
//        baos.reset() // baosbaos
//        image.compress(Bitmap.CompressFormat.JPEG, options, baos) // options%baos
//        options -= 10 // 10
//    }
//    val isBm = ByteArrayInputStream(
//        baos.toByteArray()
//    ) // baosByteArrayInputStream
//    return BitmapFactory.decodeStream(isBm, null, null)
//}

fun shrinkMethod(file: String?, width: Int, height: Int): Bitmap? {
    val bitopt = BitmapFactory.Options()
    bitopt.inJustDecodeBounds = true
    var bit = BitmapFactory.decodeFile(file, bitopt)
    val h = Math.ceil((bitopt.outHeight / height.toFloat()).toDouble()).toInt()
    val w = Math.ceil((bitopt.outWidth / width.toFloat()).toDouble()).toInt()
    if (h > 1 || w > 1) {
        if (h > w) {
            bitopt.inSampleSize = h
        } else {
            bitopt.inSampleSize = w
        }
    }
    bitopt.inJustDecodeBounds = false
    bit = BitmapFactory.decodeFile(file, bitopt)
    return bit
}

@Composable
fun CardInfoPickerScreen(navController: NavController, cardTitle: String) {
    val context = LocalContext.current

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    var hasImage by remember { mutableStateOf(false) }
    val dialogShowVal = remember { mutableStateOf(false) }
    val directory = File(context.filesDir, "images")

    val storageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri ->
        imageUri = uri
        val testImageUri: Uri = uri
        hasImage = true
//        imageUri?.let { getRealPathFromURI(context, it)
//            val kek: String? = getRealPathFromURI(context, it)
//            Log.d("DATA RECEIVE FROM COMPOSABLES: ", "KEK IS $kek")
//        }

        //TODO I/O STREAMS IMPLEMENTATION
        /**
         * НАЛАДИТЬ СТРИМ К ФАЙЛУ ЧТОБЫ СЧИТАТЬ ЕГО ДАТУ
         * ЗАПИСАТЬ ДАТУ В МАССИВ
         * ПОЛУЧИТЬ БИТМАПУ ИЗ МАССИВА
         *
         *
         *
         *
         * */
        val inputS = context.contentResolver.openInputStream(testImageUri) //GET STREAM FROM FILE
        inputS?.let {

            val byteArray = ByteArray(it.available()) //CREATE AN ARRAY
            it.read(byteArray) //GET DATA FROM STREAM TO AN ARRAY
            /** NOW THE DATA FROM THE CHOSEN FILE SHOULD BE WRITTEN AT THE $byteArray */
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

           // val compressedBitmap = compressImage(bitmap)
            //val compressedByteArray = ByteArray(it.available())
            val tempFile:File = File.createTempFile( //CREATE NEW TEMP FILE AT THE SAME DIRECTORY AS CAMERA LAUNCHER
                "temp_file_selected_picture",
                ".jpg",
                directory )

            val outS: OutputStream = FileOutputStream(tempFile) //OPEN STREAM TO THE TEMPFILE
           // bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outS)
            outS.write(byteArray) //SET DATA FROM STREAM TO AN ARRAY
            /** NOW THE DATA FROM THE $byteArray SHOULD BE WRITTEN AT THE $tempFile */
            outS.flush()
            outS.close()
            inputS.close()

            compressImageFile(context, tempFile)
            mCardImage = tempFile.toString()
            Log.d("DATA RECEIVE FROM COMPOSABLES: ", "TEMP FILE IS $tempFile")
        }

    }


    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { cameraLauncherResult: Boolean ->
            hasImage = cameraLauncherResult
            if (hasImage){
                compressImageFile(context, testGlobalTempFile)
            }
        })


    /** IMPORTANT TO ADD IT BEFORE RELEASE */
    //if (!directory.exists()) { directory.mkdirs() }

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
                    } ) {

                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    } } )



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
                       // mCardBitmap = bitmap.value
                            hasImage = false
                        //  mCardImage = imageUri.toString()
                            Log.d("DATA RECEIVE FROM COMPOSABLES: ",
                                "mCardBitmap is $mCardImage")

//                            val testResolver = context.contentResolver
//                            val kek = testResolver.openInputStream(it)
//                            val bitKek = BitmapFactory.decodeStream(kek)
//                            Log.d("DATA RECEIVE FROM COMPOSABLES: ", "bitkek is $bitKek")

                        } } }

                if (dialogShowVal.value) {
                    SourceDialog(
                        closeDialog = { dialogShowVal.value = false },
                        runStorageLauncher = { storageLauncher.launch("image/*") },
                        runCameraLauncher = {
                            val tempFile: File = File.createTempFile(
                                "temp_file_selected_picture",
                                ".jpg", directory )

                            val newPhotoContentUri = FileProvider.getUriForFile(
                                context,
                                BuildConfig.APPLICATION_ID + ".fileprovider",
                                tempFile )

                            imageUri = newPhotoContentUri

                            mCardImage = tempFile.toString() /** TODO назначил картинкой для класса
                        путь к временному файлу созданному камера лаунчером*/
                            testGlobalTempFile = tempFile
                            Log.d("DATA RECEIVE FROM COMPOSABLES: ",
                                "newPhotoContentUri is $newPhotoContentUri")
                            cameraLauncher.launch(newPhotoContentUri)
//                            if (hasImage){
//                                compressImageFile(context, tempFile)
//                            }
                        } ) }

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
                } }

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
                              //  mCardBitmap = btm

                            }
                        } else {
                            DefaultImage()
                        } }

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
                            } ) } }
                Column(modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.End
                ) {
                    AcceptNewCardButton {
                        saveTheCard(context, navController)
                    }
                } } } } }

fun saveTheCard(context: Context, navController: NavController){
//    val stream = ByteArrayOutputStream()
//    mCardBitmap?.compress(Bitmap.CompressFormat.JPEG, 0, stream)
//    val mCardByteArray: ByteArray = stream.toByteArray()

    when {
        mCardName == "" -> Toast.makeText(context, "Please, name your card", Toast.LENGTH_LONG).show()
        mCardDescription == "" -> Toast.makeText(
            context,
            "Please, describe your card",
            Toast.LENGTH_LONG
        ).show()
        mCardDate == "PICK YOUR DATE" -> Toast.makeText(context, "Please, add a date to your card", Toast.LENGTH_LONG)
            .show()
        mCardLocation == "" -> Toast.makeText(context, "Please, choose a location for your card", Toast.LENGTH_LONG)
            .show()
        mCardImage == "" -> Toast.makeText(context, "Please, select an image for your card", Toast.LENGTH_LONG)
            .show()

        else -> {
            val createdCard: Card = Card(
                0,
                mCardName,
                mCardDescription,
                mCardDate,
                mCardLocation,
                mCardImage)
            val dbHandler = SQLiteHelper(context)

            val addPlace = dbHandler.addCard(createdCard)
            Toast.makeText(context, "createdCard ${createdCard.getName()}", Toast.LENGTH_SHORT).show()

            if (addPlace > 0) {
                Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
                navController.navigate(route = Screens.MainScreen.route)
            }
            //else {Toast.makeText(context, "huy akoyta", Toast.LENGTH_SHORT).show()}
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
        ) } }

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
            Log.d("DATA RECEIVE FROM COMPOSABLES: ",
                "mCardDate is $mCardDate")
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
    } }

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
        } }

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
                } } } ) {
        Text(
            text = title,
            fontSize = 20.sp
        ) } }

/** OTHER SURFACES */

@Composable
fun TextFields(isLong: Boolean, title: String) {

    var cardTitle: String by remember {
        mutableStateOf("") }

    var cardDescription: String by remember {
        mutableStateOf("") }

    if (isLong) {

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            value = cardDescription,
            onValueChange = { cardDescription = it
                mCardDescription = cardDescription
                Log.d("DATA RECEIVE FROM COMPOSABLES: ",
                    "mCardDescription is $mCardDescription")},
            singleLine = false,
            label = { Text(text = title) } )

    } else {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = cardTitle,
            onValueChange = {
                if (it.length <= 20){
                    cardTitle = it
                    mCardName = cardTitle
                    Log.d("DATA RECEIVE FROM COMPOSABLES: ",
                        "mCardName is $mCardName")
                }
            },
            singleLine = true,
            label = {
                Text(text = title)
            } ) } }

@Composable
fun SourceDialog(
    closeDialog: () -> Unit,
    runStorageLauncher: () -> Unit,
    runCameraLauncher: () -> Unit
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
                ) } } ) }

@Composable
fun PickedImage(bitmap: Bitmap) {
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp)),
    ) }

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
            ) ) }

@Composable
fun AcceptNewCardButton(saveCard: () -> Unit) {

    FloatingActionButton(
        onClick = {
                  saveCard()
        },
        backgroundColor = MaterialTheme.colors.surface,
    ) {
        Icon(
            Icons.Filled.Done, contentDescription = null,
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
        CardInfoPickerScreen(navController = rememberNavController(), cardTitle ="you" )
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