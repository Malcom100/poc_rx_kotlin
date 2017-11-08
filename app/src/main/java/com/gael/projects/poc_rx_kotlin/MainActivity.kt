package com.gael.projects.poc_rx_kotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var button_simple_obs : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_simple_obs = button_rx_observable_create
        button_simple_obs.setOnClickListener(this)

        button_rx_observable_interval.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.button_rx_observable_create -> { createSimpleObservable()}
            R.id.button_rx_observable_interval -> { createObservableInterval()}
        }
    }

    private fun createSimpleObservable () {
        var subscr  = object : Observer<String> {

            override fun onError(t: Throwable?) {
                Log.i("Test", "on error -> ".plus(t!!.message))
            }

            override fun onNext(t: String?) {
                Log.i("Test", "on next ".plus(t!!))
            }

            override fun onComplete() {
                Log.i("Test", "on complete ")
            }

            override fun onSubscribe(d: Disposable?) {
                Log.i("Test", "on subscripe ")
            }

        }
        Observable.just("one","two","three","four","five","six")
                //execution observable
                .subscribeOn(Schedulers.newThread())
                //execution subscriber
                .observeOn(AndroidSchedulers.mainThread())
                //modify items
                .map { t -> "gael is ".plus(t).plus(" :)") }
                .subscribe(subscr)

    }

    private fun createObservableInterval () {
        var tt  = Observable.interval(0,1,TimeUnit.SECONDS)
                //execution observable
                .subscribeOn(Schedulers.newThread())
                //execution subscriber
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil(object : Predicate<Long> {
                    override fun test(t: Long): Boolean {
                        return t > 100
                    }

                })
                .subscribe(object : Observer<Long> {
                    override fun onError(e: Throwable?) {
                        Log.i("Test","on error ".plus(e!!.message))
                    }

                    override fun onNext(t: Long?) {
                        Log.i("Test",t!!.toString())
                        text_interval_result.text = "result from interval is ".plus(t!!)
                    }

                    override fun onComplete() {
                        Log.i("Test"," finish ")
                    }

                    override fun onSubscribe(d: Disposable?) {
                        Log.i("Test","on subscribe")
                    }

                })

    }
}
