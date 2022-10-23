package com.example.castingplaces

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class SQLiteHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION)
{
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "CastingPlaces.db"
        private const val TABLE_CARDS = "cards"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_CARD_NAME = "card_name"
        private const val COLUMN_CARD_DESCRIPTION = "card_description"
        private const val COLUMN_CARD_DATE = "card_date"
        private const val COLUMN_CARD_LOCATION = "card_location"
        private const val COLUMN_CARD_IMAGE = "card_image"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CARDS_TABLE = ("CREATE TABLE " + TABLE_CARDS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_CARD_NAME + " TEXT"
                + COLUMN_CARD_DESCRIPTION + " TEXT"
                + COLUMN_CARD_DATE + " TEXT"
                + COLUMN_CARD_LOCATION + " TEXT"
                + COLUMN_CARD_IMAGE + " TEXT)")
        db?.execSQL(CREATE_CARDS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(" DROP TABLE IF EXISTS $TABLE_CARDS")
        onCreate(db)
    }

    fun addCard (card: Card){
        val db: SQLiteDatabase = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_CARD_NAME, card.getName())
        values.put(COLUMN_CARD_DESCRIPTION, card.getDescription())
        values.put(COLUMN_CARD_DATE, card.getDate())
        values.put(COLUMN_CARD_LOCATION, card.getLocation())
        values.put(COLUMN_CARD_IMAGE, card.getImage())

        db.insert(TABLE_CARDS, null, values)
        db.close()
    }

    fun getCard(cardId: Int){
        val db: SQLiteDatabase = this.readableDatabase
        val selectQuery = "SELECT * FROM " + TABLE_CARDS + " WHERE " + COLUMN_ID + " = " + cardId
        Log.d("SOURCE IS: ", "$selectQuery")

        val cursor: Cursor = db.rawQuery(selectQuery, null)
        cursor.moveToFirst()

        /** SHOULD IMPLEMENT NEW CLASS OBJECT, PUT NEW INFO INTO IT AND THEN RETURN IT */

    }

}