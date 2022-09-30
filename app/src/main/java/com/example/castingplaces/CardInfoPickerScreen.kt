package com.example.castingplaces

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.Toast
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.castingplaces.ui.theme.CastingPlacesTheme
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*



@Composable
private fun DatePickerButton(){

    val context = LocalContext.current

    var calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    var pickedDate = mutableStateOf("PICK YOUR DATE")
    var buttonText by pickedDate

    val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener {
            view, slctdyear, slctdmonth, slctddayOfMonth ->

        buttonText = "$slctddayOfMonth/${slctdmonth+1}/$slctdyear"

    }, currentYear, currentMonth, currentDay)
    
    OutlinedButton(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .border(BorderStroke(Dp.Hairline, MaterialTheme.colors.onSurface)),

        onClick = {
            dpd.show()
        }) {

        Text(text ="$buttonText")
    }
}

@Composable
fun CardInfoPickerScreen(navController: NavController, cardTitle: String) {

    val mainContext = LocalContext.current

    var text: String by remember {
        mutableStateOf("")
    }

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

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = text,
                    onValueChange = { text = it },
                    singleLine = true,
                    label = { Text(text = "Title") }
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    value = text,
                    onValueChange = { text = it },
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
                        onClick = { /*TODO*/ }) {
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

//@Composable
//fun DateButton(title: String) {
//
//    val mainContext = LocalContext.current
//
//    val year = calendar.get(Calendar.YEAR)
//    val month = calendar.get(Calendar.MONTH)
//    val day = calendar.get(Calendar.DAY_OF_MONTH)
//
//    val dateFormat = "dd.MM.yyyy"
//
//    var selectedDate: String = ""
//
//    val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
//    val dateSetListener = DatePickerDialog(mainContext, DatePickerDialog.OnDateSetListener {
//            view, slctdyear, slctdmonth, slctddayOfMonth ->
//
//        selectedDate = "$slctddayOfMonth/${slctdmonth+1}/$slctdyear"
//
//        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
//
//
//
//    },
//        year,
//        month,
//        day)
//    var tiitle = mutableStateOf(selectedDate)
//        //sdf.format(cal.time).toString()
//
//
//    OutlinedButton(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(50.dp)
//            .border(BorderStroke(Dp.Hairline, MaterialTheme.colors.onSurface)),
//        onClick = {
//
//            DatePickerDialog(
//                mainContext, dateSetListener,
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH)
//            ).show()
//
//        }
//    ) {
//        Text(text = tiitle.toString() )
//    }
//}

@Composable
fun OutTextField(textIn: String) {
    var text = textIn
    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        placeholder = {
            Text(text = "more than 4")
//            if (text.length > 3) {
//                Text(text = "more than 4")
//            } else Text(text = text)
        },
        label = { Text(text = "aaaaaaaa") }
    )

}

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
        CardInfoPickerScreen(rememberNavController(), "Pick yr new place")
    }
}