package com.example.castingplaces

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.castingplaces.ui.theme.CastingPlacesTheme
import java.io.File
import java.io.OutputStream


@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val db = SQLiteHelper(context)
    val allCardsList: MutableList<Card> = remember {
        db.getAllCards()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top

        ) {
            Toolbar(title = "Casting Places")
            CardList(navController, allCardsList)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End

        ) {
            FloatButton(navController)
        }
    }
}

@Composable
fun MainCard(title: String, subtitle: String, imageUri: String, navController: NavController) {
    val context = LocalContext.current
    val qooqoo: Bitmap? = BitmapFactory.decodeFile(imageUri)
    //TODO мб тут превращать стрингу в путь
//    imageUri.let {
//        val source = ImageDecoder.createSource(context.contentResolver, imageUri)
//        bitmap.value = ImageDecoder.decodeBitmap(source)
//
//    }
    //val outS#

    /** LAGAET */
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(150.dp)
//            .padding(horizontal = 20.dp)
//            .padding(top = 10.dp)
//            .clickable {
//                navController.navigate(route = Screens.CardDetailsScreen.route)
//            },
//        shape = MaterialTheme.shapes.medium,
//        elevation = 5.dp,
//
//        ) {
//        Row(
//            modifier = Modifier
//                .fillMaxSize(0.2f)
//                .padding(10.dp),
//        horizontalArrangement = Arrangement.Start,
//        verticalAlignment = Alignment.CenterVertically)
//        {
//            Column(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .width(120.dp)
//
//
//            ) {
//                if (qooqoo != null) {
//                    Image(
//                        contentDescription = null,
//                        contentScale = ContentScale.Crop,
//                        bitmap = qooqoo.asImageBitmap(),
//                        modifier = Modifier
//                            .clip(RoundedCornerShape(10.dp)),
//                    )
//                } else
//                    DefaultCardImage()
//            }
//            DefaultCardImage()
////            Column(
////                modifier = Modifier
////                    .fillMaxSize()
////            ) {
////
////            }
//
//        }
//
//    }
//}


/** LAGAET */

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = 20.dp)
            .padding(top = 10.dp)
            .clickable {
                navController.navigate(route = Screens.CardDetailsScreen.route)
            },
        shape = MaterialTheme.shapes.medium,
        elevation = 5.dp,

        ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start

        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(120.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start

            ) {
                if (qooqoo != null) {

                    Image(
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        bitmap = qooqoo.asImageBitmap(),
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp)),
                    )
                } else DefaultCardImage()
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start

            ) {

                Text(
                    text = title,
                    fontSize = 30.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                )
                Text(text = subtitle)
            }
        }
    }
}

@Composable
fun CardList(
    navController: NavController,
    list: MutableList<Card>
) {

    LazyColumn() {
        items(items = list) { list ->
            MainCard(
                title = list.getName(),
                subtitle = list.getDescription(),
                imageUri = list.getImage(),
                navController = navController
            )
        }
    }
}

@Composable
fun DefaultCardImage() {
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
    ) }

@Composable
fun Toolbar(title: String) {
    TopAppBar(
        title = { Text(text = title) },
    )
}

@Composable
fun FloatButton(navController: NavController) {
    val mainContext = LocalContext.current
    FloatingActionButton(
        onClick = {
            Toast.makeText(mainContext, "coming soon", Toast.LENGTH_SHORT).show()
            navController.navigate(route = Screens.CardInfoPickerScreen.route)
        },
        backgroundColor = MaterialTheme.colors.surface,
    ) {
        Icon(
            Icons.Filled.Add, contentDescription = null,
            modifier = Modifier
                .size(50.dp),
            contentColorFor(backgroundColor = MaterialTheme.colors.surface)
        )
    }
}


/** ================= */

/** PREVIEWS */

//@Preview
//@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
/** fun  TopBarPreview()  {*/
//    CastingPlacesTheme {
//        Toolbar("Casting Places")
//    }
//}

//@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
/**fun CardPreview() {*/
//    CastingPlacesTheme{
//    MainCard(title = "Test" , subtitle = "CARDcard")
//    }
//}

//@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
/**fun CardListPreview() {*/
//    CastingPlacesTheme{
//        CardList()
//    }
//}

//@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun FloatingButtonPreview() {
//    CastingPlacesTheme{
//        FloatButton()
//    }
//}


@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainPreview1() {
    CastingPlacesTheme {
        MainCard(
            title = "Test",
            subtitle = "testest",
            imageUri = "/data/data/com.example.castingplaces/files/images/temp_file_selected_picture157354779621164714.jpg",
            navController = rememberNavController()
        )
    }
}