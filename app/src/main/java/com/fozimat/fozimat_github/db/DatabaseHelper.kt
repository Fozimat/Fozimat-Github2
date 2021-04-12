package com.fozimat.fozimat_github.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.fozimat.fozimat_github.db.DatabaseContract.NoteColumns
import com.fozimat.fozimat_github.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "db_user"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                " (${NoteColumns.LOGIN} TEXT NOT NULL PRIMARY KEY, " +
                " ${NoteColumns.NAME} TEXT NOT NULL," +
                " ${NoteColumns.LOCATION} TEXT NOT NULL," +
                " ${NoteColumns.REPOSITORY} INT NOT NULL," +
                " ${NoteColumns.COMPANY} TEXT NOT NULL," +
                " ${NoteColumns.FOLLOWERS} INT NOT NULL," +
                " ${NoteColumns.FOLLOWING} INT NOT NULL," +
                " ${NoteColumns.AVATAR} TEXT NOT NULL," +
                " ${NoteColumns.FAVORITE} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


}