package io.github.keddnyo.amazware.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
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

        requireActivity().title = getString(R.string.telegram) // New title

        // Setting WebView
        val webView = requireActivity().findViewById<WebView>(R.id.telegramView)
        val webSettings = webView?.settings
        webSettings!!.javaScriptEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            webSettings.forceDark = WebSettings.FORCE_DARK_OFF
        }

        // Loading WebView
        webView.loadUrl(url)
        webView.pageDown(true)
        webView.webViewClient = object : WebViewClient() {
            // Error
            override fun onReceivedError(webView: WebView, errorCode: Int, description: String, failingUrl: String) {
                webView.loadUrl("about:blank")
                requireActivity().title = getString(R.string.error)
            }
        }

        // Telegram button
        val telegramButton = requireActivity().findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.open_telegram)
        telegramButton.setOnClickListener {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=$name")))
        }
    }
}