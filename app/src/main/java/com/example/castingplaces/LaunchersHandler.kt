package com.example.castingplaces

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.FileProvider
import com.example.castingplaces.ui.theme.CastingPlacesTheme
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class LaunchersHandler : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CastingPlacesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                }
            }
        }
    }
}

@Composable
fun StorageLauncher(context: Context, directory: File): List<String>{
    val kek = mutableListOf<String>()
    val storageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri ->
        kek.add(uri.toString())
        kek.add("true")
        //imageUri = uri
        //hasImage = true
        //val copyImageUri: Uri = uri
        val inputS = context.contentResolver.openInputStream(uri) //GET STREAM FROM FILE
        inputS?.let {

            val byteArray = ByteArray(it.available())
            it.read(byteArray)                                  //GET DATA FROM STREAM TO AN ARRAY
            /** NOW THE DATA FROM THE CHOSEN FILE SHOULD BE WRITTEN AT THE $byteArray */

            /** NOW THE DATA FROM THE CHOSEN FILE SHOULD BE WRITTEN AT THE $byteArray */

            val tempFile: File = File.createTempFile(
                "temp_file_selected_picture",
                ".jpg",
                directory
            )
            val outS: OutputStream = FileOutputStream(tempFile) //OPEN STREAM TO THE TEMPFILE
            outS.write(byteArray)
            /** NOW THE DATA FROM THE $byteArray SHOULD BE WRITTEN AT THE $tempFile */
            /** NOW THE DATA FROM THE $byteArray SHOULD BE WRITTEN AT THE $tempFile */
            outS.flush()
            outS.close()
            inputS.close()

            compressImageFile(context, tempFile)
            mCardImage = tempFile.toString()
            Log.d("DATA RECEIVE FROM COMPOSABLES: ", "TEMP FILE IS $tempFile")
        }
    }
    return kek
}

@Composable
fun CopyImageToOwnDirectory(context: Context, directory: File, uri: Uri){
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

        compressImageFile(context, tempFile)
        mCardImage = tempFile.toString()
        Log.d("DATA RECEIVE FROM COMPOSABLES: ", "TEMP FILE IS $tempFile")
    }
}

@Composable
fun SaveImageToOwnDirectory(context: Context, directory: File): Uri {
    val tempFile: File = File.createTempFile(
        "temp_file_selected_picture",
        ".jpg", directory
    )

    val newPhotoContentUri = FileProvider.getUriForFile(
        context,
        BuildConfig.APPLICATION_ID + ".fileprovider",
        tempFile
    )

    mCardImage = tempFile.toString()
    /** TODO назначил картинкой для класса
    путь к временному файлу созданному камера лаунчером*/

    testGlobalTempFile = tempFile
    Log.d(
        "DATA RECEIVE FROM COMPOSABLES: ",
        "newPhotoContentUri is $newPhotoContentUri"
    )
    return newPhotoContentUri
}

fun storageLauncherrr(context: Context, directory: File): List<String>{
    val kek = mutableListOf<String>()
    val storageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri ->
        kek.add(uri.toString())
        kek.add("true")
        //imageUri = uri
        //hasImage = true
        //val copyImageUri: Uri = uri
        val inputS = context.contentResolver.openInputStream(uri) //GET STREAM FROM FILE
        inputS?.let {

            val byteArray = ByteArray(it.available())
            it.read(byteArray)                                  //GET DATA FROM STREAM TO AN ARRAY
            /** NOW THE DATA FROM THE CHOSEN FILE SHOULD BE WRITTEN AT THE $byteArray */

            /** NOW THE DATA FROM THE CHOSEN FILE SHOULD BE WRITTEN AT THE $byteArray */

            val tempFile: File = File.createTempFile(
                "temp_file_selected_picture",
                ".jpg",
                directory
            )
            val outS: OutputStream = FileOutputStream(tempFile) //OPEN STREAM TO THE TEMPFILE
            outS.write(byteArray)
            /** NOW THE DATA FROM THE $byteArray SHOULD BE WRITTEN AT THE $tempFile */
            /** NOW THE DATA FROM THE $byteArray SHOULD BE WRITTEN AT THE $tempFile */
            outS.flush()
            outS.close()
            inputS.close()

            compressImageFile(context, tempFile)
            mCardImage = tempFile.toString()
            Log.d("DATA RECEIVE FROM COMPOSABLES: ", "TEMP FILE IS $tempFile")
        }
    }
    return kek
}
