package com.gael.projects.poc_rx_kotlin.interfaces

import java.io.InputStream

/**
 * Created by gael on 13.11.17.
 */

interface IFile {

    fun saveStream(inputStream : InputStream)
    fun showName()
    fun updateTextView()
}