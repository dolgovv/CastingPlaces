package com.example.castingplaces

import android.graphics.Bitmap
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

public abstract class Card {
    abstract var _id: Int
    abstract var _name: String
    abstract var _description: String
    abstract var _date: String
    abstract var _location: String
    abstract var _image: Bitmap


    fun getId(): Int {
        return _id
    }

    fun setId(id: Int) {
        this._id = id
    }

    fun getName(): String {
        return _name
    }

    fun setName(name: String) {
        this._name = name
    }

    fun getDescription(): String {
        return _description
    }

    fun setDescription(description: String) {
        this._description = description
    }

    fun getDate(): String {
        return _date
    }

    fun setDate(date: String) {
        this._date = date
    }

    fun getLocation(): String {
        return _location
    }

    fun setLocation(location: String) {
        this._location = location
    }

    fun getImage(): ByteArray {
        val stream = ByteArrayOutputStream()
        _image.compress(Bitmap.CompressFormat.JPEG, 0, stream)
        return stream.toByteArray()
    }

    fun setImage(image: Bitmap) {
        this._image = image
    }
}