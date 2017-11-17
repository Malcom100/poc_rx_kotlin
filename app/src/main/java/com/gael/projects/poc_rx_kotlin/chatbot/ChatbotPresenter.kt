package com.gael.projects.poc_rx_kotlin.chatbot

/**
 * Created by gael on 09.11.17.
 */

class ChatbotPresenter (mView : ChatbotContract.View): ChatbotContract.Presenter {

    private lateinit var view : ChatbotContract.View
    init {
        view = mView
        view.setPresenter(this)
    }
}