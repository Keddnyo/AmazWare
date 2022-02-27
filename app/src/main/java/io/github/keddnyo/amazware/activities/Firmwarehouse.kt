package io.github.keddnyo.amazware.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.github.keddnyo.amazware.R
import io.github.keddnyo.amazware.utils.Download
import io.github.keddnyo.amazware.utils.MakeRequest
import io.github.keddnyo.amazware.utils.DarkMode

class Firmwarehouse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)

        title = getString(R.string.firmwarehouse) // New title

        val sp =
            PreferenceManager.getDefaultSharedPreferences(this) // Shared Preferences

        DarkMode().switch(this) // Set theme

        val permissionCheck = ActivityCompat.checkSelfPermission( // Permissions
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
            )
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onResume() {
        super.onResume()

        val webView = findViewById<WebView>(R.id.webView)
        val webSettings = webView?.settings
        val refresh = findViewById<SwipeRefreshLayout>(R.id.cloud_refresh)

        webSettings!!.javaScriptEnabled = true
        webView.loadUrl(MakeRequest().openExplorePage(this)) // Loading WebView
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                url: String
            ): Boolean { // Override loading
                return if (url.startsWith("tg:")) {
                    startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    )
                    webView.goBack()
                    true
                } else {
                    false
                }
            }

            override fun onReceivedError( // Error
                webView: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                webView.loadUrl("about:blank")
                title = getString(R.string.error)
            }
        }

        webView.setDownloadListener { fileUrl, _, contentDisposition, mimeType, _ -> // Downloading code
            Download().run(this, fileUrl, contentDisposition, mimeType)
        }

        refresh.setOnRefreshListener { // Pull refresh
            title = getString(R.string.firmwarehouse)
            webView.reload()
            refresh.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.feed -> {
                startActivity(Intent(this, Feed::class.java))
            }
            R.id.extras -> {
                startActivity(Intent(this, Extras::class.java))
            }
            R.id.settings -> {
                startActivity(Intent(this, Settings::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}