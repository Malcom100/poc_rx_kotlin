package com.gael.projects.poc_rx_kotlin.fingerprint

import com.gael.projects.poc_rx_kotlin.BasePresenter
import com.gael.projects.poc_rx_kotlin.BaseView

/**
 * Created by gael on 09.11.17.
 */

interface FingerprintContract {

    interface View : BaseView<Presenter> {}
    interface Presenter : BasePresenter {}
}