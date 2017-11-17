package com.gael.projects.poc_rx_kotlin.databse

import android.content.Context
import java.util.*

/**
 * Created by gael on 14.11.17.
 */

object DBManager {
    private lateinit var appDBHelper : MyDatabase
    fun initializeDB(nameDB : String, vers : Int, context :Context) {
        appDBHelper = MyDatabase(context,nameDB,vers)

        appDBHelper.writableDatabase
    }
}
