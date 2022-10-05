package com.example.castingplaces

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.Image
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImageViewModel : ViewModel() {
    private val pickedBitmap: MutableLiveData<List<Bitmap>> by lazy {
        MutableLiveData<List<Bitmap>>().also {
            loadUri()
        }
    }

    fun getImageBitmap(): LiveData<List<Bitmap>> {
        return pickedBitmap
    }

    private fun loadUri() {
        // Do an asynchronous operation to fetch users.
    }

    val pickedUri by mutableStateOf("")

}