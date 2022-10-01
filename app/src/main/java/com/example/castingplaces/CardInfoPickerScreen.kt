package com.example.castingplaces

import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.castingplaces.ui.theme.CastingPlacesTheme
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*


@Composable
fun CardInfoPickerScreen(navController: NavController, cardTitle: String) {

    val openDialog = remember { mutableStateOf(false) }

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
                title = { Text(text = cardTitle) },
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
                if (openDialog.value) {
                    AlertDialog(
                        onDismissRequest = {
                            openDialog.value = false
                        },
                        title = {
                            Text(text = "Choose source")
                        },
                        buttons = {
                            Row(
                                modifier = Modifier.padding(all = 8.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                PermGrantButtons(
                                    "Gallery",
                                    "Camera"
                                )
                            }
                        }
                    )
                }

                var cardTitle: String by remember {
                    mutableStateOf("")
                }
                var cardDescription: String by remember {
                    mutableStateOf("")
                }


                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = cardTitle,
                    onValueChange = { cardTitle = it },
                    singleLine = true,
                    label = { Text(text = "Title") }
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    value = cardDescription,
                    onValueChange = { cardDescription = it },
                    singleLine = false,
                    label = { Text(text = "Description") })

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
                        Image(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp))
                                .border(
                                    border = BorderStroke(
                                        Dp.Hairline,
                                        MaterialTheme.colors.onSurface
                                    ),
                                    shape = RectangleShape
                                ),
                            painter = painterResource(id = R.drawable.ic_launcher_background),
                            contentDescription = "pick yr image"
                        )
                    }

                    TextButton(modifier = Modifier
                        .fillMaxSize(),
                        onClick = {

                            openDialog.value = true

                        }) {
                        Text(text = "ADD IMAGE")
                    }
                }

                Button(modifier = Modifier
                    .fillMaxWidth(),
                    onClick = { /*TODO*/ }) {
                    Text(text = "SAVE")

                }
            }
        }
    }
}

@Composable
private fun DatePickerButton() {

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
fun PermGrantButtons(
    /**    1ая кнопка: если нет перма -> диалог. на принятие ->
    НЕ показывает диалог отказа.
    на отказ ->
    показывает диалог отказа.
    2ая кнопка спрашивает перм если его еще нет*/
    firstButtonTitle: String,
    secondButtonTitle: String
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        /** ЭТО ДИАЛОГ И ЕГО ФУНКЦИОНАЛ */
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        /** ЛОГИКА ПОЛУЧИЛ ИЛИ НЕТ РАЗРЕШЕНИЯ */
        if (isGranted) {
            /** закрыть  */
            // Permission Accepted: Do something
            Log.d("ExampleScreen", "PERMISSION GRANTED")

        } else {
            // Permission Denied: Do something
            Log.d("ExampleScreen", "PERMISSION DENIED")
        }
    }

    TextButton( //  НАЖИМЕШЬ И ЛИБО ДЕЛАЕТ ЧТО НАДО ЛИБО ПРОСИТ РЕЗРЕШЕНИЙ
        modifier = Modifier.width(100.dp),
        onClick = {
            /** ПОКАЗАТЬ ДИАЛОГ ЕСЛИ ПЕРМИШН НЕ ДАН */
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) -> {
                    /**  Some works that require permission */

                    Log.d("ExampleScreen", "Code requires permission")
                }

                else -> {
                    // Asking for permission
                    launcher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }

        }) {
        Text(text = firstButtonTitle)
    }

    TextButton( //  НАЖИМЕШЬ И ЛИБО ДЕЛАЕТ ЧТО НАДО ЛИБО ПРОСИТ РЕЗРЕШЕНИЙ
        modifier = Modifier.width(100.dp),
        onClick = {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.CAMERA
                ) -> {
                    // Some works that require permission
                    Log.d("ExampleScreen", "Code requires permission")
                }
                else -> {
                    // Asking for permission
                    launcher.launch(android.Manifest.permission.CAMERA)
                }

            }
        }) {
        Text(text = secondButtonTitle)
    }
}


/** ======= PREVIEWS ======= */

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview3() {
//    CastingPlacesTheme {
//        CardInfoPickerScreen(navController = rememberNavController(), "Card Info Picker")
//    }
//}


@Preview(showBackground = true)
@Composable
fun FieldPreview() {
    CastingPlacesTheme {
        DatePickerButton()
    }
}