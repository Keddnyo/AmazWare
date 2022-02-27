package io.github.keddnyo.amazware.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.github.keddnyo.amazware.fragments.utils.Download
import io.github.keddnyo.amazware.R
import java.util.*

class Explore : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = getString(R.string.explore) // New title

        val lang = Locale.getDefault().language.toString()
        val webView = requireActivity().findViewById<WebView>(R.id.webView)
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(requireContext()) // Shared Preferences
        val refresh =
            requireActivity().findViewById<SwipeRefreshLayout>(R.id.cloud_refresh)

        val webSettings = webView?.settings
        webSettings!!.javaScriptEnabled = true

        // Set theme
        val theme = when (sharedPreferences.getString("theme", "1")) {
            "1" -> {
                "light"
            }
            "2" -> {
                "dark"
            }
            "3" -> {
                "auto" // Dummy (not matter)
            }
            else -> {
                "auto" // Dummy (not matter)
            }
        }

        // Loading WebView
        val url: Uri.Builder = Uri.Builder()
        url.scheme("https")
            .authority("schakal.ru")
            .appendPath("fw")
            .appendPath("firmwares_list.htm")
            .appendQueryParameter("theme", theme)
            .appendQueryParameter("lang", lang)
        webView.loadUrl(url.toString())

        webView.webViewClient = object : WebViewClient() {
            // Override loading
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
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
            // Error
            override fun onReceivedError(
                webView: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                webView.loadUrl("about:blank")
                requireActivity().title = getString(R.string.error)
            }
        }

        // Downloading code
        webView.setDownloadListener { fileUrl, _, contentDisposition, mimeType, _ ->
            context?.let { Download().run(it, fileUrl, contentDisposition, mimeType) }
        }

        // Pull refresh
        refresh.setOnRefreshListener {
            requireActivity().title = getString(R.string.explore)
            webView.reload()
            refresh.isRefreshing = false
        }
    }
}