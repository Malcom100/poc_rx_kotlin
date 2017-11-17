package com.gael.projects.poc_rx_kotlin.chatbot

import com.gael.projects.poc_rx_kotlin.BasePresenter
import com.gael.projects.poc_rx_kotlin.BaseView

/**
 * Created by gael on 09.11.17.
 */

interface ChatbotContract {
    interface View : BaseView<Presenter> {}
    interface Presenter : BasePresenter{}
}