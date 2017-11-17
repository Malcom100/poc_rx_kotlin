package com.gael.projects.poc_rx_kotlin.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.support.v4.app.ActivityCompat

/**
 * Created by gael on 09.11.17.
 */

class PermissionsUtils {
    companion object {

        val CODE_FINGERPRINT = 587
        val CODE_FINGERPRINT_CANCELLATION_SIGNAL = 588
        val CODE_VIBRATE = 589
        val CODE_WRITE_FILE = 590

        fun askPermissionFingerPrint(context : Activity) {
            ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.USE_FINGERPRINT), CODE_FINGERPRINT)
        }

        fun askPermissionFingerPrintForCancellationSigna(context : Activity) {
            ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.USE_FINGERPRINT), CODE_FINGERPRINT_CANCELLATION_SIGNAL)
        }

        fun askPermissionVibration(context : Activity) {
            ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.VIBRATE), CODE_VIBRATE)
        }

        fun askPermissionWriteFile(context : Activity) {
            ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), CODE_WRITE_FILE)
        }
    }
}