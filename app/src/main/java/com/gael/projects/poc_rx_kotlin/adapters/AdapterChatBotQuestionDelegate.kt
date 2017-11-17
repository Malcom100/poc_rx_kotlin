package com.gael.projects.poc_rx_kotlin.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gael.projects.poc_rx_kotlin.R
import com.gael.projects.poc_rx_kotlin.commons.ViewType
import com.gael.projects.poc_rx_kotlin.commons.ViewTypeDelegateAdapter
import com.gael.projects.poc_rx_kotlin.models.QuestionViewType

/**
 * Created by gael on 10.11.17.
 */

class AdapterChatBotQuestionDelegate(cxt : Context) : ViewTypeDelegateAdapter {
    private lateinit var context : Context
    init {
        context = cxt
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return QuestionViewHolder(LayoutInflater.from(context).inflate(R.layout.item_questions,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as QuestionViewHolder
        item as QuestionViewType
        holder.question.text = "App : ".plus(item.question)
    }

    class QuestionViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        lateinit var question: TextView
        init {
            question = view.findViewById(R.id.question)
        }
    }
}