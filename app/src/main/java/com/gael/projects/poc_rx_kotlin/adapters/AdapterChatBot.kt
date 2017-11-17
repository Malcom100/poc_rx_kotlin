package com.gael.projects.poc_rx_kotlin.adapters

import android.content.Context
import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import com.gael.projects.poc_rx_kotlin.commons.AdapterConstants
import com.gael.projects.poc_rx_kotlin.commons.ViewType
import com.gael.projects.poc_rx_kotlin.commons.ViewTypeDelegateAdapter
import com.gael.projects.poc_rx_kotlin.models.ChatBot
import com.gael.projects.poc_rx_kotlin.models.QuestionViewType
import com.gael.projects.poc_rx_kotlin.models.ResponseViewType

/**
 * Created by gael on 10.11.17.
 */

class AdapterChatBot (cxt : Context, obj: ChatBot) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context : Context
    private lateinit var items : ArrayList<ViewType>
    //delegate manager
    private var deleagetAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
    private lateinit var chatBot : ChatBot
    init {
        context = cxt
        items = ArrayList()
        chatBot = obj
        deleagetAdapters.put(AdapterConstants.response,AdapterChatBotResponseDelegate(context,chatBot))
        deleagetAdapters.put(AdapterConstants.questions, AdapterChatBotQuestionDelegate(context))
    }

    // create the good view holder for adapter
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return deleagetAdapters.get(viewType).onCreateViewHolder(parent!!)
    }

    //gives the good view holder for adapter
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        deleagetAdapters.get(getItemViewType(position)).onBindViewHolder(holder!!,items[position])
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].getViewType()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addNewResponse(responses : ArrayList<ResponseViewType>) {

        items.addAll(responses)
        notifyDataSetChanged()
    }

    fun addewResponse(response : ResponseViewType) {
        items.add(response)
        notifyDataSetChanged()
    }

    fun addNewQuestion(question : QuestionViewType) {
        items.add(question)
        notifyDataSetChanged()
    }

    fun addQuestions(questions : ArrayList<ResponseViewType>) {
        items.addAll(questions)
        notifyDataSetChanged()
    }

    fun displayFirstResponses() {
        items.add(ResponseViewType(getString()))
    }

    /**
     * create a string of all choices to send adapter a list with coma
     */
    fun getString() : String {
        var result = StringBuilder()
        chatBot.choices.forEach { s -> result.append(s).append(";")}
        result.deleteCharAt(result.lastIndex)
        return result.toString()
    }
}