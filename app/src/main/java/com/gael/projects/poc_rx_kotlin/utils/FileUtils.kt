package com.gael.projects.poc_rx_kotlin.utils

import android.content.Context
import android.os.Environment
import android.util.Log
import com.gael.projects.poc_rx_kotlin.databse.DBManager
import com.gael.projects.poc_rx_kotlin.databse.MyDatabase
import com.gael.projects.poc_rx_kotlin.models.ChatBot
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.nio.file.Files

/**
 * Created by gael on 09.11.17.
 */

class FileUtils {
    companion object {

        val NAME_FILE = "chatbot.json"

        val JSON_KEY_PRESENTATION = "presentation"
        val JSON_KEY_PRESENTATION_HELLO = "hello"
        val JSON_KEY_PRESENTATION_NAME = "name"
        val JSON_KEY_PRESENTATION_HELP = "help"
        val JSON_KEY_CHOICE = "choice"

        val DB_URL = "http://epmobile00.e-contactsep.com/prod/ios/"
        val DB_NAME_REMOTE = "MEPDatabase.sqlite"
        val FILE_NAME_INTERNAL = "testfiledb.db"
        val TIME_OUT = 5000
        val VERSION_DB = 2

        fun createChatBot(context : Context) : ChatBot{
            //var jsonObj = createJSON(readFileChatBot(context))
            return parseJson(createJSON(readFileChatBot(context)))
        }

        fun readFileChatBot(context : Context) : String{
            val NAME_FILE_OUTPUT = "result.txt"
            var reader : BufferedReader? = null
            var res : StringBuilder? = null
            try {
                res = StringBuilder()
                context.assets.open(NAME_FILE).bufferedReader().useLines { lines ->
                    lines.forEach {
                        res!!.append(it)
                    }
                }
                //Log.i("Test",res.toString())
            }catch (ioe : IOException) {
                ioe.printStackTrace()
                Log.e("E",ioe.message)
            }finally {
                closeBufferedReader(reader)
            }
            return res!!.toString()
        }

        private fun closeBufferedReader(bufferedReader: BufferedReader?) {
            if(bufferedReader != null) {
                try {
                    bufferedReader.close()
                }catch (ioe : IOException) {
                    ioe.printStackTrace()
                    Log.e("E",ioe.message)
                }
            }
        }

        private fun closeInputStream(inputStream: InputStream) {
            if(inputStream != null) {
                try {
                    inputStream.close()
                }catch (ioe : IOException) {
                    ioe.printStackTrace()
                    Log.e("E",ioe.message)
                }
            }
        }

        private fun closeFileOutputStream(outputStream: OutputStream) {
            if(outputStream != null) {
                try {
                    outputStream.close()
                }catch (ioe : IOException) {
                    ioe.printStackTrace()
                    Log.e("E",ioe.message)
                }
            }
        }

        private fun createJSON(value : String) : JSONObject{
            var jsonObject : JSONObject
            jsonObject = JSONObject(value)
            return jsonObject
        }

        private fun parseJson(jobj : JSONObject) : ChatBot {
            var chatbot : ChatBot = ChatBot()
            if(jobj.has(JSON_KEY_PRESENTATION)) {
                val listPresentation = ArrayList<String>()
                var presentations = jobj.getJSONObject(JSON_KEY_PRESENTATION)
                //values
                if(presentations.has(JSON_KEY_PRESENTATION_HELLO)) {
                    listPresentation.add(presentations.getString(JSON_KEY_PRESENTATION_HELLO))
                }
                if(presentations.has(JSON_KEY_PRESENTATION_NAME)) {
                    listPresentation.add(presentations.getString(JSON_KEY_PRESENTATION_NAME))
                }
                if(presentations.has(JSON_KEY_PRESENTATION_HELP)) {
                    listPresentation.add(presentations.getString(JSON_KEY_PRESENTATION_HELP))
                }
                chatbot.presentation = listPresentation.toTypedArray()
                //end values

                val listChoice = ArrayList<String>()
                if(jobj.has(JSON_KEY_CHOICE)) {
                    var choices = jobj.getJSONArray(JSON_KEY_CHOICE)

                    //values
                    for(i in 0..(choices.length() - 1)){
                        val obj = choices.getJSONObject(i)
                        listChoice.add(obj.getString(JSON_KEY_CHOICE))
                    }
                    chatbot.choices = listChoice.toTypedArray()
                    //end values
                }
            }
            return chatbot
        }

        fun readInputStream(inputStream : InputStream, cxt :Context) : String{
            /*var res : StringBuilder = StringBuilder()
            inputStream.bufferedReader().useLines { lines ->
                lines.forEach { s ->
                    //Log.i("Test",s)
                    res.append(s)
                }
            }*/
            createFile(cxt,inputStream)
            return FILE_NAME_INTERNAL
        }

        fun createFile(context: Context,inputStream: InputStream) {
            if(!existsFile(context)) {
                Log.i("Test","file does not again exists ")
            }else {
                Log.i("Test","file exists ")
                val deleted = getFile(context).delete()
                Log.i("Test","file is deleted ? "+deleted)
            }
            val file = File(context.getExternalFilesDir(null).path.plus("/").plus(FILE_NAME_INTERNAL))
            var output : OutputStream  = FileOutputStream(file)
            try {
                val buffer = ByteArray(4 * 1024)
                var read : Int = inputStream.read(buffer)
                while (read != -1) {
                    output.write(buffer,0,read)
                    read = inputStream.read(buffer)
                }
            }finally {
                closeInputStream(inputStream)
                closeFileOutputStream(output)
            }
            if(file != null) {
                Log.i("Test",file.path+" "+file.usableSpace)
            }else {
                Log.i("Test","file is null")
            }

            DBManager.initializeDB(FILE_NAME_INTERNAL, VERSION_DB,context)
        }

        fun existsFile(context: Context) : Boolean{
            return getFile(context).exists()
        }

        fun getFile(context: Context) : File {
            return File (context.getExternalFilesDir(null).path.plus("/").plus(FILE_NAME_INTERNAL))
        }

    }
}