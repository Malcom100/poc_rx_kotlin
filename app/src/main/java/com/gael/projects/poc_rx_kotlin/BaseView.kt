package com.gael.projects.poc_rx_kotlin

/**
 * Created by gael on 09.11.17.
 */

interface BaseView<T>{
    fun setPresenter(mPresenter : T)
}