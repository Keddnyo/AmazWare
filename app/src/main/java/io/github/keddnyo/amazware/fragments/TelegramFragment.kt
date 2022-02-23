package io.github.keddnyo.amazware.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.method.Touch.scrollTo
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import io.github.keddnyo.amazware.R

class TelegramFragment : Fragment() {

    private val name = "firmwarehouse"
    private val url = "https://xn--r1a.website/s/$name"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_telegram, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity!!.title = getString(R.string.telegram)

        val webView = activity!!.findViewById<WebView>(R.id.telegramView)
        val webSettings = webView?.settings
        webSettings!!.javaScriptEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            webSettings.forceDark = WebSettings.FORCE_DARK_ON
        }
        webView.clearHistory()
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
        webView.loadUrl(url)
        webView.pageDown(true)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return if (url.startsWith("https://tlgr.org/")) {
                    startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=$name"))
                    )
                    webView.goBack()
                    true
                } else {
                    false
                }
            }
            override fun onReceivedError(webView: WebView, errorCode: Int, description: String, failingUrl: String) {
                webView.loadUrl("about:blank")
                val alertDialog = AlertDialog.Builder(activity)
                alertDialog.setTitle(getString(R.string.error))
                alertDialog.setMessage(getString(R.string.retry_connect))
                alertDialog.setNegativeButton(getString(R.string.refresh)) { dialog, _ ->
                    dialog.dismiss()
                    webView.reload()
                    webView.loadUrl(url)
                }
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }

        val telegramButton = activity!!.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.open_telegram)
        telegramButton.setOnClickListener {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=$name")))
        }
    }
}