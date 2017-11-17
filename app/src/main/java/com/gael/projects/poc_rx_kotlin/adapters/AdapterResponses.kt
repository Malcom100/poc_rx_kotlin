package com.gael.projects.poc_rx_kotlin.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.gael.projects.poc_rx_kotlin.R
import kotlinx.android.synthetic.main.item_responses_btn.view.*

/**
 * Created by gael on 10.11.17.
 */

class AdapterResponses (cxt : Context, list : ArrayList<String>) : RecyclerView.Adapter<AdapterResponses.ResponsesViewHolder>(){

    private lateinit var context : Context
    private lateinit var items : ArrayList<String>
    init {
        context = cxt
        items = list
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ResponsesViewHolder {
        var view : View = LayoutInflater.from(context).inflate(R.layout.item_responses_btn,parent,false)
        return ResponsesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResponsesViewHolder?, position: Int) {
        holder!!.buttonQuestion.text = items[position]
        holder!!.buttonQuestion.setOnClickListener(View.OnClickListener { v ->
            Toast.makeText(context,items.get(position).plus(" is your choice "),Toast.LENGTH_LONG)
                    .show()
        })
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ResponsesViewHolder (view : View): RecyclerView.ViewHolder(view) {
        lateinit var buttonQuestion : TextView
        init {
            buttonQuestion = view.findViewById(R.id.btn_question)
        }
    }
}