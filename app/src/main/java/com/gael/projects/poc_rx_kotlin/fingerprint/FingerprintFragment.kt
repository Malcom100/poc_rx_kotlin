package com.gael.projects.poc_rx_kotlin.fingerprint

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.gael.projects.poc_rx_kotlin.MainActivity
import com.gael.projects.poc_rx_kotlin.R
import kotlinx.android.synthetic.main.*
import kotlinx.android.synthetic.main.fragment_fingerprint.*

/**
 * Created by gael on 09.11.17.
 */

class FingerprintFragment :Fragment(), FingerprintContract.View, View.OnTouchListener {

    private lateinit var presenter : FingerprintContract.Presenter
    private lateinit var activity : MainActivity

    private var alreadySent = false

    companion object {
        fun newInstance() : FingerprintFragment {
            return FingerprintFragment()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(context is MainActivity){
            activity = context as MainActivity
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_fingerprint,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        image_fingerprint.setOnTouchListener(this)
    }

    override fun setPresenter(mPresenter: FingerprintContract.Presenter) {
        presenter = mPresenter
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if(v!!.id == R.id.image_fingerprint && !alreadySent){
            alreadySent = true
            activity.fingerprintAuth()
            return true
        }
        return false
    }
}