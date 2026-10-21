package nl.rijmaatje.app

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var web: WebView
    private val permissionRequest = 7001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        web = WebView(this)
        web.settings.javaScriptEnabled = true
        web.settings.domStorageEnabled = true
        web.settings.setGeolocationEnabled(true)
        web.webViewClient = WebViewClient()
        web.addJavascriptInterface(AndroidBridge(), "RijmaatjeAndroid")
        web.webChromeClient = object : WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(
                origin: String?,
                callback: GeolocationPermissions.Callback?
            ) {
                callback?.invoke(origin, true, false)
            }
        }
        setContentView(web)
        web.loadUrl("file:///android_asset/index.html")
        requestAppPermissions()
    }

    inner class AndroidBridge {
        @JavascriptInterface
        fun startLocationService() {
            runOnUiThread {
                val intent = Intent(this@MainActivity, LocationService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(intent)
                else startService(intent)
            }
        }

        @JavascriptInterface
        fun stopLocationService() {
            runOnUiThread {
                stopService(Intent(this@MainActivity, LocationService::class.java))
            }
        }
    }

    private fun requestAppPermissions() {
        val needed = mutableListOf(Manifest.permission.ACCESS_FINE_LOCATION)
        if (Build.VERSION.SDK_INT >= 33) needed.add(Manifest.permission.POST_NOTIFICATIONS)
        val missing = needed.filter {
            checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
        }
        if (missing.isNotEmpty()) requestPermissions(missing.toTypedArray(), permissionRequest)
    }

    override fun onBackPressed() {
        if (web.canGoBack()) web.goBack() else super.onBackPressed()
    }

    override fun onDestroy() {
        web.destroy()
        super.onDestroy()
    }
}

