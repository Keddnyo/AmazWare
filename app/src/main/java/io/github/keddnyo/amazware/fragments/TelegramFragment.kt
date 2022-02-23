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
            webSettings.forceDark = WebSettings.FORCE_DARK_OFF
        }
        webView.clearHistory()
        webView.loadUrl(url)
        webView.pageDown(true)

        val telegramButton = activity!!.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.open_telegram)
        telegramButton.setOnClickListener {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=$name")))
        }
    }
}