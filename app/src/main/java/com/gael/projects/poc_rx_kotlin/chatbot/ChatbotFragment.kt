package com.gael.projects.poc_rx_kotlin.chatbot

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gael.projects.poc_rx_kotlin.R
import com.gael.projects.poc_rx_kotlin.adapters.AdapterChatBot
import com.gael.projects.poc_rx_kotlin.models.ChatBot
import com.gael.projects.poc_rx_kotlin.models.QuestionViewType
import com.gael.projects.poc_rx_kotlin.models.ResponseViewType
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_chatbot.*
import java.util.concurrent.TimeUnit

/**
 * Created by gael on 09.11.17.
 */

class ChatbotFragment : Fragment(), ChatbotContract.View {

    private lateinit var presenterChatBot : ChatbotContract.Presenter

    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter : AdapterChatBot

    private lateinit var chatBot : ChatBot

    companion object {
        val KEY_CHATBOT = "key_chatbot"

        fun newInstance() = ChatbotFragment

        fun newInstance(chatbot : ChatBot) : ChatbotFragment {
            var instance = ChatbotFragment()
            var arguments : Bundle = Bundle()
            arguments.putParcelable(KEY_CHATBOT,chatbot)
            instance.arguments = arguments
            return instance
        }
    }

    override fun setPresenter(mPresenter: ChatbotContract.Presenter) {
        presenterChatBot = mPresenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        manageArguments(arguments)
    }

    private fun manageArguments(arguments : Bundle) {
        if(arguments != null && arguments.containsKey(KEY_CHATBOT)) {
            chatBot = arguments.getParcelable(KEY_CHATBOT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_chatbot,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        chatBot.readChoices()
        chatBot.readPresentations()
        adapter = AdapterChatBot(activity,chatBot)
        recyclerView = recyclerview
        var linearLayoutManager : LinearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter

        sendFirstQuestions()
    }

    private fun sendFirstQuestions() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .take(3)
                .subscribe(object : Observer<Long> {
                    override fun onError(e: Throwable?) {
                        Log.i("Test", "on error ".plus(e!!.message))
                    }

                    override fun onNext(t: Long?) {
                        //Log.i("Test", chatBot.presentation.get(t!!.toInt()))
                        adapter.addNewQuestion(QuestionViewType(chatBot.presentation.get(t!!.toInt())))
                    }

                    override fun onComplete() {
                        Log.i("Test", " finish ")
                        adapter.displayFirstResponses()
                    }

                    override fun onSubscribe(d: Disposable?) {
                        Log.i("Test", "on subscribe")
                    }

                })

    }

}