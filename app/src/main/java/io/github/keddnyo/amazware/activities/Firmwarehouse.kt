package io.github.keddnyo.amazware.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.github.keddnyo.amazware.R
import io.github.keddnyo.amazware.utils.DarkMode
import io.github.keddnyo.amazware.utils.Download
import io.github.keddnyo.amazware.utils.MakeRequest

class Firmwarehouse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firmwarehouse)

        title = getString(R.string.firmwarehouse) // New title

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

        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)
        val webView = findViewById<WebView>(R.id.webView)
        val webSettings = webView?.settings
        val refresh = findViewById<SwipeRefreshLayout>(R.id.cloud_refresh)
        val floatingButton = findViewById<FloatingActionButton>(R.id.favouriteButtonFirmwarehouse)

        if ((sharedPreferences.getString("deviceSource", "") != "")
        ) {
            floatingButton.visibility = View.VISIBLE
        } else {
            floatingButton.visibility = View.GONE
        }

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

        webView.setDownloadListener { fileUrl, _, _, _, _ -> // Downloading code
            Download().run(this, fileUrl)
        }

        refresh.setOnRefreshListener { // Pull refresh
            title = getString(R.string.firmwarehouse)
            webView.reload()
            refresh.isRefreshing = false

            title = getString(R.string.firmwarehouse)
        }

        floatingButton.setOnClickListener {
            val deviceSource = sharedPreferences.getString("deviceSource", "")
            val url = "https://schakal.ru/fw/firmwares_list.htm?device=$deviceSource"
            webView.loadUrl(url)
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
                startActivity(Intent(this, ExtrasDialog::class.java))
            }
            R.id.settings -> {
                startActivity(Intent(this, Settings::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}