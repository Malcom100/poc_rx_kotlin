package com.gael.projects.poc_rx_kotlin.models

import android.os.Parcel
import android.os.Parcelable
import android.util.Log

/**
 * Created by gael on 09.11.17.
 */

class ChatBot() : Parcelable{

    lateinit var presentation : Array<String>
    lateinit var choices : Array<String>

    constructor(parcel: Parcel) : this() {
        presentation = parcel.createStringArray()
        choices = parcel.createStringArray()
    }

    fun readPresentations () {
        presentation.forEach { s -> Log.i("Test",s) }
    }

    fun readChoices() {
        choices.forEach { s -> Log.i("Test",s) }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringArray(presentation)
        parcel.writeStringArray(choices)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChatBot> {
        override fun createFromParcel(parcel: Parcel): ChatBot {
            return ChatBot(parcel)
        }

        override fun newArray(size: Int): Array<ChatBot?> {
            return arrayOfNulls(size)
        }
    }

}