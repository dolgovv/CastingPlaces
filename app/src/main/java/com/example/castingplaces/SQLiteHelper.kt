package com.example.castingplaces

import android.R.id
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class SQLiteHelper(context: Context) :
SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{
    companion object {
        private const val DATABASE_VERSION = 3
        private const val DATABASE_NAME = "CastingPlaces.db"
        private const val TABLE_CARDS = "cards"

        private const val COLUMN_ID = "_id"
        private const val COLUMN_CARD_NAME = "name"
        private const val COLUMN_CARD_DESCRIPTION = "description"
        private const val COLUMN_CARD_DATE = "date"
        private const val COLUMN_CARD_LOCATION = "location"
        private const val COLUMN_CARD_IMAGE = "image"

    }


    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CARDS_TABLE = ("CREATE TABLE " + TABLE_CARDS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_CARD_NAME + " TEXT,"
                + COLUMN_CARD_DESCRIPTION + " TEXT,"
                + COLUMN_CARD_DATE + " TEXT,"
                + COLUMN_CARD_LOCATION + " TEXT,"
                + COLUMN_CARD_IMAGE + " TEXT)")
        db?.execSQL(CREATE_CARDS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(" DROP TABLE IF EXISTS $TABLE_CARDS")
        onCreate(db)
    }

    fun addCard (card: Card): Long {
        val db: SQLiteDatabase = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_CARD_NAME, card.getName())
        values.put(COLUMN_CARD_DESCRIPTION, card.getDescription())
        values.put(COLUMN_CARD_DATE, card.getDate())
        values.put(COLUMN_CARD_LOCATION, card.getLocation())
        values.put(COLUMN_CARD_IMAGE, card.getImage())

        val result = db.insert(TABLE_CARDS, null, values)
        db.close()
        return result
    }

    /** NEED TO BE TESTED ?whereArgs? */
    fun updateCard(card: Card): Int {
        val db = this.writableDatabase
        val whereArgs = arrayOf<String>(java.lang.String.valueOf(card.getId()))

        val values = ContentValues()
        values.put(COLUMN_CARD_NAME, card.getName())
        values.put(COLUMN_CARD_DESCRIPTION, card.getDescription())
        values.put(COLUMN_CARD_DATE, card.getDate())
        values.put(COLUMN_CARD_LOCATION, card.getLocation())
        values.put(COLUMN_CARD_IMAGE, card.getImage())

        return db.update(TABLE_CARDS, values, COLUMN_ID + " =?", whereArgs)
    }

    fun deleteCard(card: Card){
        val db = this.writableDatabase
        val whereArgs = arrayOf<String>(java.lang.String.valueOf(card.getId()))

        db.delete(TABLE_CARDS, COLUMN_ID + " =?", whereArgs)
        db.close()
    }
    /** === === NOT TO USE === === */

    fun getCard(cardId: Int): Card {
        val db: SQLiteDatabase = this.readableDatabase
        val selectQuery = "SELECT * FROM " + TABLE_CARDS + " WHERE " + COLUMN_ID + " = " + cardId
        Log.d("SOURCE IS: ", "$selectQuery")

        val curs: Cursor = db.rawQuery(selectQuery, null)
        curs.moveToFirst()

        val newCard: Card = Card(
            curs.getInt(curs.getColumnIndex(COLUMN_ID)), // ID
            curs.getString(curs.getColumnIndex(COLUMN_CARD_NAME)), // NAME
            curs.getString(curs.getColumnIndex(COLUMN_CARD_DESCRIPTION)), // DESCRIPTION
            curs.getString(curs.getColumnIndex(COLUMN_CARD_DATE)), // DATE
            curs.getString(curs.getColumnIndex(COLUMN_CARD_LOCATION)), // LOCATION
            curs.getString(curs.getColumnIndex(COLUMN_CARD_IMAGE)) // IMAGE (ByteArray)
            )

        return newCard
    }

    fun getAllCards(): MutableList<Card> {

        val newCardsList: MutableList<Card> = ArrayList<Card>()
        val selectQuery = "SELECT * FROM " + TABLE_CARDS
        val db = this.writableDatabase
        val curs: Cursor = db.rawQuery(selectQuery, null)

        if (curs.moveToFirst()){
            do {
                val newCard: Card = Card(
                    curs.getInt(curs.getColumnIndex(COLUMN_ID)), // ID
                    curs.getString(curs.getColumnIndex(COLUMN_CARD_NAME)), // NAME
                    curs.getString(curs.getColumnIndex(COLUMN_CARD_DESCRIPTION)), // DESCRIPTION
                    curs.getString(curs.getColumnIndex(COLUMN_CARD_DATE)), // DATE
                    curs.getString(curs.getColumnIndex(COLUMN_CARD_LOCATION)), // LOCATION
                    curs.getString(curs.getColumnIndex(COLUMN_CARD_IMAGE)) // IMAGE (ByteArray)
                )
                newCardsList.add(newCard)
            } while (curs.moveToNext())
        }
        return newCardsList
    }
}