package com.example.castingplaces

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.lazy.items
import android.os.Bundle
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.castingplaces.ui.theme.CastingPlacesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {

            CastingPlacesTheme {

                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavController) {

    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top

        ) {
            Toolbar(title = "Casting Places")
            CardList()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End

            ) {
                FloatButton()
            }
        }
    }
}

@Composable
fun MainCard(title: String, subtitle: String) {
    val context = LocalContext.current
    val intent = Intent(context, CardInfo::class.java)
    val stringTEST = "xdcfvgbhnjmk"
    intent.putExtra("1", stringTEST)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = 20.dp)
            .padding(top = 10.dp)
            .clickable {
                context.startActivity(intent)
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

            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Card`s image",
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
            )

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
fun CardList(list: List<String> = List(2) { "$it" }, cardTitle: String = "Unknown kartochka") {
    LazyColumn() {
        items(items = list) { list ->
            MainCard(title = cardTitle, subtitle = "pizdec, uzhe $list kartochka")
        }
    }
}

@Composable
fun Toolbar(title: String) {
    TopAppBar(
        title = { Text(text = title) },
    )
}

@Composable
fun FloatButton() {
    val mainContext = LocalContext.current
    FloatingActionButton(
        onClick = {
            Toast.makeText(mainContext, "coming soon", Toast.LENGTH_SHORT).show()
        },
        backgroundColor = MaterialTheme.colors.surface,
    ) {
        Icon(
            Icons.Filled.Add, contentDescription = null,
            modifier = Modifier
                .size(50.dp),
            contentColorFor(backgroundColor = MaterialTheme.colors.onSurface)
        )
    }
}

/** ================= */

@Composable
fun CardDetailsScreen(cardTitle : String?){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top

        ) {
            Toolbar(title = "$cardTitle")

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End

            ) {
                FloatButton()
            }
        }
    }
}


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


@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainPreview() {
    CastingPlacesTheme {
        MainScreen()
    }
}