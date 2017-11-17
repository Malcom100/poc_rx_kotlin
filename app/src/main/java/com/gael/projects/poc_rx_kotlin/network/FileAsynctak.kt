package com.gael.projects.poc_rx_kotlin.network

import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.gael.projects.poc_rx_kotlin.MainActivity
import com.gael.projects.poc_rx_kotlin.R
import com.gael.projects.poc_rx_kotlin.interfaces.IFile
import com.gael.projects.poc_rx_kotlin.utils.FileUtils
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by gael on 13.11.17.
 */

class FileAsynctak(cxt : Context, activity: IFile) : AsyncTask<String, Void, String>() {

    private lateinit var context : Context
    private lateinit var activity : IFile
    init {
        context = cxt
        this.activity = activity
    }
    private var dialog : Dialog? = null



    override fun onPreExecute() {
        super.onPreExecute()

        dialog = Dialog(context)
        dialog!!.setContentView(R.layout.dialog_file_asynctask)
        dialog!!.show()

    }

    override fun doInBackground(vararg params: String?): String {

        var result : String = ""

        try {
            val url = URL (params[0])
            val connect = url.openConnection() as HttpURLConnection

            //GET
            connect.requestMethod = "GET"
            //time out
            connect.readTimeout = FileUtils.TIME_OUT
            connect.connectTimeout = FileUtils.TIME_OUT
            //output
            connect.doInput = true
            connect.connect()

            val responseCode : Int = connect.responseCode
            if(responseCode != HttpURLConnection.HTTP_OK) {
                Log.i("Test","response code is "+responseCode)
            }else {
                val input : InputStream = connect.inputStream
                if(input != null) {
                    //result = FileUtils.readInputStream(input,context)
                    activity.saveStream(input)
                    activity.showName()
                    if(dialog != null && dialog!!.isShowing) {
                        dialog!!.dismiss()
                    }
                }
            }
        }catch (e :Exception) {
            e.printStackTrace()
            Log.e("E",e.message)
        }
        return result
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        if(dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }

        //activity.showName()
    }

}