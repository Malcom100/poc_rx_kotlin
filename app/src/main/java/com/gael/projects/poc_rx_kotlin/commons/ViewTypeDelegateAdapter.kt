package com.gael.projects.poc_rx_kotlin.commons

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by gael on 10.11.17.
 */

interface ViewTypeDelegateAdapter {
    fun onCreateViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder
    fun onBindViewHolder (holder : RecyclerView.ViewHolder, item : ViewType)
}