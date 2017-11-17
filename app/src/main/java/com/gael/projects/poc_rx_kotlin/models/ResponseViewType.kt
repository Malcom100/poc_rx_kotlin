package com.gael.projects.poc_rx_kotlin.models

import android.os.Parcel
import android.os.Parcelable
import com.gael.projects.poc_rx_kotlin.commons.AdapterConstants
import com.gael.projects.poc_rx_kotlin.commons.ViewType

/**
 * Created by gael on 10.11.17.
 */

class ResponseViewType(): ViewType, Parcelable{

    lateinit var response : String

    constructor(parcel: Parcel) : this() {
        response = parcel.readString()
    }

    constructor(resp : String) : this(){
        response = resp
    }

    override fun getViewType() = AdapterConstants.response
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(response)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResponseViewType> {
        override fun createFromParcel(parcel: Parcel): ResponseViewType {
            return ResponseViewType(parcel)
        }

        override fun newArray(size: Int): Array<ResponseViewType?> {
            return arrayOfNulls(size)
        }
    }
}