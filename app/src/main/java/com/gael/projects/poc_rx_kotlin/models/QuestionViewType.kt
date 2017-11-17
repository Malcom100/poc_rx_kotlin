package com.gael.projects.poc_rx_kotlin.models

import android.os.Parcel
import android.os.Parcelable
import com.gael.projects.poc_rx_kotlin.commons.AdapterConstants
import com.gael.projects.poc_rx_kotlin.commons.ViewType

/**
 * Created by gael on 10.11.17.
 */

class QuestionViewType(): ViewType, Parcelable {

    lateinit var question : String

    constructor(parcel: Parcel) : this() {
        question = parcel.readString()
    }

    constructor(question : String) : this(){
        this.question = question
    }
    override fun getViewType(): Int {
        return AdapterConstants.questions
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(question)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<QuestionViewType> {
        override fun createFromParcel(parcel: Parcel): QuestionViewType {
            return QuestionViewType(parcel)
        }

        override fun newArray(size: Int): Array<QuestionViewType?> {
            return arrayOfNulls(size)
        }
    }

}