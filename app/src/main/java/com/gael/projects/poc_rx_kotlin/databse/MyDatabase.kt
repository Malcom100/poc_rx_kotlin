package com.gael.projects.poc_rx_kotlin.databse

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQuery
import android.os.Environment
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper

/**
 * Created by gael on 14.11.17.
 */

class MyDatabase(cxt : Context, databaseName : String, version :Int) :
        SQLiteAssetHelper(cxt,databaseName,cxt.getExternalFilesDir(null).absolutePath,null,version){

    private lateinit var mContext : Context
    private lateinit var mDatabase : String
    private var mVersionDB : Int? =null
    init {
        mContext = cxt
        mDatabase = databaseName
        mVersionDB = version
    }
}