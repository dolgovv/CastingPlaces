package com.example.castingplaces

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.io.IOException

class MapActivity : ComponentActivity() {



    companion object {
        const val MAP_REQUEST_CODE = 12
        private var mLatitude: Double = 0.0 // A variable which will hold the latitude value.
        private var mLongitude: Double = 0.0 // A variable which will hold the longitude value.

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val button: Button = findViewById(R.id.btn_getloc)

        if (!Places.isInitialized()) {
            Places.initialize(
                this@MapActivity,
                resources.getString(R.string.casting_places_api_key)
            )
        }
        try {
            // These are the list of fields which we required is passed
            val fields = listOf(
                Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG,
                Place.Field.ADDRESS
            )
            // Start the autocomplete intent with a unique request code.
            val mapIntent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            ).build(this@MapActivity)
            startActivityForResult(mapIntent, MAP_REQUEST_CODE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
        public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK) {

                if (requestCode == MAP_REQUEST_CODE) {

                    val place: Place = Autocomplete.getPlaceFromIntent(data!!)

                    mLocation =     place.address as String
                    mCardLocation.value = place.address as String
                    mLatitude = place.latLng!!.latitude
                    mLongitude = place.latLng!!.longitude
                    Log.d("map opener lol", "$mCardLocation.value")
                    finish()
                }
                // END
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("map opener lol", "Cancelled")
                finish()
            }
        }
    }

