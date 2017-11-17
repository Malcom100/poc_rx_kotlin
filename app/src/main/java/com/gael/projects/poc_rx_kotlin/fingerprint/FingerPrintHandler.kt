package com.gael.projects.poc_rx_kotlin.fingerprint

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.CancellationSignal
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import com.gael.projects.poc_rx_kotlin.MainActivity
import com.gael.projects.poc_rx_kotlin.utils.PermissionsUtils

/**
 * Created by gael on 09.11.17.
 */

class FingerPrintHandler (cxt : Context): FingerprintManager.AuthenticationCallback () {
    private lateinit var context : Context
    init {
        context = cxt
    }
    private lateinit var cancellationSignal : CancellationSignal

    /**
     * initialize the fingerprint authentification
     */
    fun startAuth(manager : FingerprintManager, cryptoOBJECT : FingerprintManager.CryptoObject) {
        cancellationSignal = CancellationSignal()

        if(ActivityCompat.checkSelfPermission(context,Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        manager.authenticate(cryptoOBJECT,cancellationSignal,0,this,null)
    }

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
        super.onAuthenticationError(errorCode, errString)
        Toast.makeText(context,
                "Authentication error\n" + errString,
                Toast.LENGTH_LONG).show()
        notifyMainActivity("Fingerprint authentification error")
    }

    override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()

        Toast.makeText(context,
                "Authentication failed.",
                Toast.LENGTH_LONG).show()

        notifyMainActivity("Fingerprint authentification failed")
    }

    override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
        super.onAuthenticationSucceeded(result)

        Toast.makeText(context,
                "Authentication succeeded.",
                Toast.LENGTH_LONG).show()
        notifyMainActivity("Fingerprint authentification succeeded")
    }

    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
        super.onAuthenticationHelp(helpCode, helpString)
        notifyMainActivity("Fingerprint authentification help")
    }

    private fun notifyMainActivity(r : String) {
        (context as MainActivity).getResult(r)
    }
}