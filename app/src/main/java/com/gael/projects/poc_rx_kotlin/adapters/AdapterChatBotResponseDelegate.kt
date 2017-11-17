package com.gael.projects.poc_rx_kotlin.adapters

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gael.projects.poc_rx_kotlin.R
import com.gael.projects.poc_rx_kotlin.commons.ViewType
import com.gael.projects.poc_rx_kotlin.commons.ViewTypeDelegateAdapter
import com.gael.projects.poc_rx_kotlin.models.ChatBot
import com.gael.projects.poc_rx_kotlin.models.ResponseViewType

/**
 * Created by gael on 10.11.17.
 */
class AdapterChatBotResponseDelegate(cxt : Context, obj : ChatBot): ViewTypeDelegateAdapter {

    private lateinit var context : Context
    private lateinit var chatBot : ChatBot
    private lateinit var adapter : AdapterResponses
    init {
        context = cxt
        chatBot = obj
        adapter = AdapterResponses(context,chatBot.choices.toCollection(ArrayList()))
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ResponseViewHolder(LayoutInflater.from(context).inflate(R.layout.item_response,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as ResponseViewHolder
        item as ResponseViewType
        var linearLayout : LinearLayoutManager = GridLayoutManager(context,chatBot.choices.size)
        holder.recyclerView.layoutManager = linearLayout
        holder.recyclerView.adapter = adapter
    }

    class ResponseViewHolder (view : View): RecyclerView.ViewHolder(view) {
        lateinit var recyclerView : RecyclerView
        init {
            recyclerView = view.findViewById(R.id.reyclerview_responses)
        }
    }

}