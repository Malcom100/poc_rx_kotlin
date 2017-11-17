package com.gael.projects.poc_rx_kotlin

import android.Manifest
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.GradientDrawable
import android.hardware.fingerprint.FingerprintManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.support.v4.app.ActivityCompat
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.gael.projects.poc_rx_kotlin.chatbot.ChatbotContract
import com.gael.projects.poc_rx_kotlin.chatbot.ChatbotFragment
import com.gael.projects.poc_rx_kotlin.chatbot.ChatbotPresenter
import com.gael.projects.poc_rx_kotlin.fingerprint.FingerPrintHandler
import com.gael.projects.poc_rx_kotlin.fingerprint.FingerprintContract
import com.gael.projects.poc_rx_kotlin.fingerprint.FingerprintFragment
import com.gael.projects.poc_rx_kotlin.fingerprint.FingerprintPresenter
import com.gael.projects.poc_rx_kotlin.interfaces.IFile
import com.gael.projects.poc_rx_kotlin.models.ChatBot
import com.gael.projects.poc_rx_kotlin.network.FileAsynctak
import com.gael.projects.poc_rx_kotlin.utils.FileUtils
import com.gael.projects.poc_rx_kotlin.utils.PermissionsUtils
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.io.InputStream
import java.lang.RuntimeException
import java.security.*
import java.security.cert.CertificateException
import java.util.concurrent.TimeUnit
import javax.crypto.*

class MainActivity : AppCompatActivity(), View.OnClickListener, IFile {

    private val KEY_NAME = "example_fingerprint_key"

    private lateinit var button_simple_obs: Button

    private lateinit var presenterFingerPrint: FingerprintContract.Presenter

    private lateinit var fingerprintManager: FingerprintManager
    private lateinit var keyguardManager: KeyguardManager
    private lateinit var keystore: KeyStore
    private var keystoreContainer = "AndroidKeyStore"
    private lateinit var keyGenerator: KeyGenerator
    private var cipher: Cipher? = null
    private lateinit var cryptoObject: FingerprintManager.CryptoObject

    private lateinit var presenterChatBot : ChatbotContract.Presenter
    private lateinit var inputStream : InputStream
    private var nameFile : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        fingerprintManager = getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager


        button_simple_obs = button_rx_observable_create
        button_simple_obs.setOnClickListener(this)

        button_rx_observable_interval.setOnClickListener(this)

        button_fingerprint.setOnClickListener(this)

        if (!checkLockScreenSecurity()) {
            button_fingerprint.isEnabled = false
        }
        checkFingerPrintPermission()
        chechFingerprintIsSaved()
        generateKey()
        if (cipherInit()) {

            cryptoObject = FingerprintManager.CryptoObject(cipher)

            var helper= FingerPrintHandler(this)
            helper.startAuth(fingerprintManager,cryptoObject)
        }

        checkPermissionVIbrate()

        button_chatbot.setOnClickListener(this)
        button_file.setOnClickListener(this)
        button_anim_move.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("Test", requestCode.toString().plus(" ").plus(resultCode));
    }


    var handlerProgress = Handler()
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.button_rx_observable_create -> {
                createSimpleObservable()
            }
            R.id.button_rx_observable_interval -> {
                createObservableInterval()
            }
            R.id.button_fingerprint -> {
                fingerPrint()
            }
            R.id.button_chatbot -> {
                /*val ch = FileUtils.createChatBot(this)
                ch.readPresentations()
                ch.readChoices()*/
                gotoChatBot(FileUtils.createChatBot(this))
            }

            R.id.button_file -> {
                checkWriteFilePermission()
            }

            R.id.button_anim_move -> {

                //animation color
                var animationDrawable : GradientDrawable = test_circle.background as GradientDrawable
                animationDrawable.start()

                //text view
                text_anim_move.visibility = View.VISIBLE
                var animtopOut : Animation = AnimationUtils.loadAnimation(this, R.anim.animation_move)
                text_anim_move.animation = animtopOut

                //progress bar
                var progress : ProgressBar = circularProgressbar
                //start
                progress.progress = 0
                //end
                progress.secondaryProgress = 100
                progress.max = 100
                progress.progressDrawable = resources.getDrawable(R.drawable.custom_progress_bar)
                var pStatus = 0

                Thread(Runnable {
                    kotlin.run {
                        while(pStatus < 100) {
                            pStatus += 1

                            //set the progress
                            handlerProgress.post(Runnable {
                                percent_progress.text = pStatus.toString().plus(" %")
                                progress.progress = pStatus
                            })
                            //each second
                            try{
                                Thread.sleep(1000)
                            }catch (e : Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }).start()
            }
        }
    }


    private fun gotoChatBot(chatbot : ChatBot) {
        manageLayouts()
        var fragment : ChatbotFragment? = supportFragmentManager.findFragmentById(R.id.container_frg) as? ChatbotFragment
        if(fragment == null) {
            fragment = ChatbotFragment.newInstance(chatbot)
        }
        supportFragmentManager.beginTransaction()
                .add(R.id.container_frg,fragment,"")
                .commit()

        presenterChatBot = ChatbotPresenter(fragment)
    }

    private fun test() {
        if(cryptoObject.cipher != null) {
            try {
                var result = cryptoObject.cipher.doFinal("Very secret message".toByteArray())
                if(result != null) {
                    Log.i("Test", Base64.encodeToString(result,0 /* flags*/))
                }
            }catch (e :BadPaddingException) {
                e.printStackTrace()
                throw RuntimeException("Failed to encrypt the data with the generated key. ",e)
            }catch (e : IllegalBlockSizeException){
                e.printStackTrace()
                throw RuntimeException("Failed to encrypt the data with the generated key. ",e)
            }
        }
    }

    private fun createSimpleObservable() {
        var subscr = object : Observer<String> {

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
        Observable.just("one", "two", "three", "four", "five", "six")
                //execution observable
                .subscribeOn(Schedulers.newThread())
                //execution subscriber
                .observeOn(AndroidSchedulers.mainThread())
                //modify items
                .map { t -> "gael is ".plus(t).plus(" :)") }
                .subscribe(subscr)

    }

    private fun createObservableInterval() {
        var tt = Observable.interval(0, 1, TimeUnit.SECONDS)
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
                        Log.i("Test", "on error ".plus(e!!.message))
                    }

                    override fun onNext(t: Long?) {
                        Log.i("Test", t!!.toString())
                        text_interval_result.text = "result from interval is ".plus(t!!)
                    }

                    override fun onComplete() {
                        Log.i("Test", " finish ")
                    }

                    override fun onSubscribe(d: Disposable?) {
                        Log.i("Test", "on subscribe")
                    }

                })

    }

    private fun fingerPrint() {
        manageLayouts()
        var frg: FingerprintFragment? = supportFragmentManager.findFragmentById(R.id.container_frg) as? FingerprintFragment
        if (frg == null) {
            frg = FingerprintFragment.newInstance()

            presenterFingerPrint = FingerprintPresenter(frg)
        }
        supportFragmentManager.beginTransaction()
                .add(R.id.container_frg, frg, "")
                .commit()
    }

    private fun manageLayouts() {
            layout_buttons.visibility = View.GONE
            container_frg.visibility = View.VISIBLE
    }

    /**
     * Verify the lock screen is protected by a PIN, pattern or password
     */
    private fun checkLockScreenSecurity(): Boolean {
        var isEnable = true
        if (!keyguardManager.isKeyguardSecure) {
            isEnable = false
            Toast.makeText(this, getString(R.string.error_lock_screen_disable), Toast.LENGTH_SHORT)
                    .show()
        }
        return isEnable
    }

    private fun checkFingerPrintPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getString(R.string.message_fingerprint_permission), Toast.LENGTH_SHORT)
                    .show()
            PermissionsUtils.askPermissionFingerPrint(this)
        }
    }

    /**
     * Verify that at leats one fingerprint has registered on the device
     */
    private fun chechFingerprintIsSaved() {
        if (!fingerprintManager.hasEnrolledFingerprints()) {
            Toast.makeText(this, getString(R.string.message_fingerprint_not_saved), Toast.LENGTH_SHORT)
                    .show()
            PermissionsUtils.askPermissionFingerPrint(this)
        }
    }

    private fun generateKey() {
        //keystore container
        try {
            keystore = KeyStore.getInstance(keystoreContainer)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("E", e.message)
        }

        //KeyGenerator to generate an encryption key and store it in Keystore container
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, keystoreContainer)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            Log.e("E", e.message)
            throw RuntimeException(" Failed to get KeyGenerator instance ", e)
        } catch (e: NoSuchProviderException) {
            e.printStackTrace()
            Log.e("E", e.message)
            throw RuntimeException(" Failed to get KeyGenerator instance ", e)
        }

        //generate the key
        try {
            keystore.load(null)
            keyGenerator.init(KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(false)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build())
            keyGenerator.generateKey()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            Log.e("E", e.message)
            throw RuntimeException(e)
        } catch (e: NoSuchProviderException) {
            e.printStackTrace()
            Log.e("E", e.message)
            throw RuntimeException(e)
        } catch (e: CertificateException) {
            throw RuntimeException(e)
        }

    }

    fun cipherInit(): Boolean {

        //get cipher
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES.plus("/")
                    .plus(KeyProperties.BLOCK_MODE_CBC).plus("/")
                    .plus(KeyProperties.ENCRYPTION_PADDING_PKCS7))
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            Log.e("E", e.message)
            throw RuntimeException(" Failed to get Cipher ", e)
        } catch (e: NoSuchProviderException) {
            e.printStackTrace()
            Log.e("E", e.message)
            throw RuntimeException(" Failed to get Cipher ", e)
        }

        //initialize cipher using the key generated
        try{
            keystore.load(null)
            var key : SecretKey = keystore.getKey(KEY_NAME,null) as SecretKey

            cipher!!.init(Cipher.ENCRYPT_MODE, key)
            return true
        }catch (e : KeyStoreException) {
            e.printStackTrace()
            throw RuntimeException(" Failed to init Cipher ", e)
        }catch (e : CertificateException) {
            e.printStackTrace()
            throw RuntimeException(" Failed to init Cipher ", e)
        }catch (e : UnrecoverableKeyException){
            e.printStackTrace()
            throw RuntimeException(" Failed to init Cipher ", e)
        }catch (e : NoSuchAlgorithmException) {
            e.printStackTrace()
            throw RuntimeException(" Failed to init Cipher ", e)
        }catch (e: IOException) {
            e.printStackTrace()
            throw RuntimeException(" Failed to init Cipher ", e)
        }catch (e : InvalidKeyException) {
            e.printStackTrace()
            throw RuntimeException(" Failed to init Cipher ", e)
        }

        return false

    }

    private fun checkPermissionVIbrate() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            PermissionsUtils.askPermissionVibration(this)
        }
    }

    fun getResult(str : String) {
        Log.i("Test",str)
    }

    fun fingerprintAuth() {
        test()
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        v.vibrate(250)
        Toast.makeText(this,getString(R.string.message_fingerprint_succeed),Toast.LENGTH_LONG)
                .show()
    }

    override fun saveStream(inputStream: InputStream) {
        this.inputStream = inputStream
    }

    override fun showName() {
        nameFile = FileUtils.readInputStream(this.inputStream,this)
    }

    fun checkWriteFilePermission () {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            PermissionsUtils.askPermissionWriteFile(this)
        }else{
            getFileFromServer()
        }
    }

    private fun getFileFromServer() {
        val req = FileAsynctak(this,this)
        req.execute(FileUtils.DB_URL+FileUtils.DB_NAME_REMOTE)
    }

    val handler = Handler()
    val run = Runnable {
        text_file.text = "File is ".plus(nameFile)
    }
    override fun updateTextView() {
        handler.post(run)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            PermissionsUtils.CODE_WRITE_FILE -> {
                if(grantResults.size > 0 && grantResults.get(0) == PackageManager.PERMISSION_GRANTED) {
                    getFileFromServer()
                }
            }
        }
    }

}
