package com.example.castingplaces

import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.icu.text.CaseMap.Title
import android.net.Uri
import android.util.Log
import android.widget.Button
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
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
import androidx.core.graphics.scale
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.castingplaces.ui.theme.CastingPlacesTheme
import java.io.File
import java.io.InputStream
import java.util.*

class ImageFileProvider2 : FileProvider() {
    companion object {
        fun getImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            val file = File.createTempFile(
                "selected_image_",
                ".jpg",
                directory,
            )
            val authority = context.packageName + ".fileprovider"
            return getUriForFile(
                context,
                authority,
                file,
            )
        }
    }
}


@Composable
fun CardInfoPickerScreen(navController: NavController, cardTitle: String) {

    val context = LocalContext.current

    /** VALUES FOR IMAGE PICKER */

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val storageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    /** CAMERA FUNC VALUES */
    var hasImage by remember {
        mutableStateOf(false)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { cameraLauncherResult ->
        hasImage = cameraLauncherResult
    }

    /** OTHER VALUES */

    val dialogShowVal = remember { mutableStateOf(false) }
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
                }
            )


            Column( //column for textFields
                modifier = Modifier
                    .fillMaxHeight(0.4f)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                if (dialogShowVal.value) {

                    SourceDialog(
                        closeDialog = { dialogShowVal.value = false },
                        runStorageLauncher = { storageLauncher.launch("image/*") },
                        runCameraLauncher = {
                            val uri = ImageFileProvider.getImageUri(context)
                            imageUri = uri
                            cameraLauncher.launch(uri)},
                        context = context
                    )
                }

                TextFields(isLong = false, title = "Title")
                TextFields(isLong = true, title = "Description")

                DatePickerButton()

                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .border(BorderStroke(Dp.Hairline, MaterialTheme.colors.onSurface)),
                    onClick = {}

                ) {
                    Text(text = "PICK A LOCATION")
                }
            }

            Column( //column for everything except textFields
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row( //row for image and text button
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
                        imageUri?.let {
                            val source = ImageDecoder
                                .createSource(context.contentResolver, it)
                            bitmap.value = ImageDecoder.decodeBitmap(source)
                        }

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
                            dialogShow = { dialogShowVal.value = true }
                        )
                    }
                }
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

    val pickedDate = mutableStateOf("PICK YOUR DATE")
    var buttonText by pickedDate

    val dpd = DatePickerDialog(
        context,
        DatePickerDialog.OnDateSetListener { _, slctdyear, slctdmonth, slctddayOfMonth ->

            buttonText = "$slctddayOfMonth/${slctdmonth + 1}/$slctdyear"

        },
        currentYear,
        currentMonth,
        currentDay
    )

    OutlinedButton(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .border(BorderStroke(Dp.Hairline, MaterialTheme.colors.onSurface)),

        onClick = {
            dpd.show()
        }) {

        Text(text = buttonText)
    }
}

@Composable
fun ButtonImageSourcePicker(
    title: String,
    onClickTest: () -> Unit,
    dialogShow: () -> Unit
) {

    val context = LocalContext.current

    /** VALUES FOR PERMISSION REQUESTING */

    val permLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            dialogShow()
            onClickTest()
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
                    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    onClickTest()
                    dialogShow()
                } else {
                    permLauncher.launch(android.Manifest.permission.CAMERA)
                    permLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }

            } else if (title == "Gallery") {
                if (ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    onClickTest()
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
            onValueChange = { cardDescription = it },
            singleLine = false,
            label = { Text(text = title) })
    } else {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = cardTitle,
            onValueChange = {
                cardTitle = if (it.length <= 20) {
                    it
                } else cardTitle
            },
            singleLine = true,
            label = {
                Text(text = title)
            }
        )
    }
}

@Composable
fun SourceDialog(
    closeDialog: () -> Unit,
    runStorageLauncher: () -> Unit,
    runCameraLauncher: () -> Unit,
    context: Context
) {
    AlertDialog( modifier = Modifier
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
                    onClickTest = {
                        runStorageLauncher()
                    },
                    dialogShow = { closeDialog() }

                )
                ButtonImageSourcePicker("Camera",
                    onClickTest = {
                        Toast.makeText(
                            context,
                            "camera clicked",
                            Toast.LENGTH_SHORT
                        ).show()

                        val cameraUri = ImageFileProvider2.getImageUri(context)

                        runCameraLauncher()

                        //imageUri = cameraUri

                    },
                    dialogShow = { closeDialog() }
                )
            }
        }
    )
}

@Composable
fun PickedImage(bitmap: Bitmap) {
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .border(
                border = BorderStroke(
                    Dp.Hairline,
                    MaterialTheme.colors.onSurface
                )
            ),
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

/** ======= PREVIEWS ======= */

//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview3() {
//    CastingPlacesTheme {
//        CardInfoPickerScreen(navController = rememberNavController(), "Card Info Picker")
//    }
//}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun FieldPreview() {
    CastingPlacesTheme {
        SourceDialog(
            closeDialog = { /*TODO*/ },
            runStorageLauncher = { /*TODO*/ },
            runCameraLauncher = { /*TODO*/ },
            context = LocalContext.current
        )
    }
}