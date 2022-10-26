package com.example.castingplaces

import android.graphics.Bitmap
import android.util.Log
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class Card(
    private var _id: Int,
    private var _name: String,
    private var _description: String,
    private var _date: String,
    private var _location: String,
    private var _image: ByteArray,
) {

    fun getId(): Int {
        Log.d("DATABASE MAINTENCE:", "got ID")
        return _id
    }

    fun setId(id: Int) {
        this._id = id
    }

    fun getName(): String {
        Log.d("DATABASE MAINTENCE:", "got Name")
        return _name
    }

    fun setName(name: String) {
        this._name = name
    }

    fun getDescription(): String {
        Log.d("DATABASE MAINTENCE:", "got Description")
        return _description
    }

    fun setDescription(description: String) {
        this._description = description
    }

    fun getDate(): String {
        Log.d("DATABASE MAINTENCE:", "got Date")
        return _date
    }

    fun setDate(date: String) {
        this._date = date
    }

    fun getLocation(): String {
        Log.d("DATABASE MAINTENCE:", "got Location")
        return _location
    }

    fun setLocation(location: String) {
        this._location = location
    }

    fun getImage(): ByteArray {
//        val stream = ByteArrayOutputStream()
//        _image.compress(Bitmap.CompressFormat.JPEG, 0, stream)
//        return stream.toByteArray()
        return _image
    }

    fun setImage(image: ByteArray) {
        this._image = image
    }
}