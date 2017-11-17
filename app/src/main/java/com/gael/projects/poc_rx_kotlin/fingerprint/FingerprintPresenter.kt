package com.gael.projects.poc_rx_kotlin.fingerprint

/**
 * Created on 09.11.17.
 */

class FingerprintPresenter (mView : FingerprintContract.View) : FingerprintContract.Presenter {

    private lateinit var view : FingerprintContract.View
    init {
        view = mView
        view.setPresenter(this)
    }
}