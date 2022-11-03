package com.example.castingplaces

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class LauncherTest: ComponentActivity() {
    fun storageLaunch(context: Context, directory: File): List<String> {
        val kek = mutableListOf<String>()

        val getContent = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            // Handle the returned Uri
            kek.add(uri.toString())
            kek.add("true")
            //imageUri = uri
            //hasImage = true
            //val copyImageUri: Uri = uri
            if (uri != null) {
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
                    val outS: OutputStream =
                        FileOutputStream(tempFile) //OPEN STREAM TO THE TEMPFILE
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
        }
    return kek
    }
}
fun kek(){

}
