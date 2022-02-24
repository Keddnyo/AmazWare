package io.github.keddnyo.amazware.fragments

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import io.github.keddnyo.amazware.R

class CloudFragment : Fragment() {

    private val url = "https://schakal.ru/fw/firmwares_list.htm"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cloud, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = getString(R.string.cloud) // New title

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context) // Shared Preferences

        // Setting WebView
        val webView = requireActivity().findViewById<WebView>(R.id.webView)
        val webSettings = webView?.settings
        webSettings!!.javaScriptEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            webSettings.forceDark = WebSettings.FORCE_DARK_ON
        }
        webView.clearHistory()

        // Loading WebView
        webView.loadUrl(url)
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
            override fun onReceivedError(webView: WebView, errorCode: Int, description: String, failingUrl: String) {
                webView.loadUrl("about:blank")
                requireActivity().title = getString(R.string.error)
            }
        }

        // Downloading code
        when {
            sharedPreferences.getBoolean("download_provider", true) -> {
                webView.setDownloadListener { url, _, contentDisposition, mimeType, _ ->
                    val request = DownloadManager.Request(Uri.parse(url))
                    request.setDescription(getString(R.string.downloading))
                    request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType))
                    request.allowScanningByMediaScanner()
                    // Notification depending on preference
                    when {
                        sharedPreferences.getBoolean("download_notification", true) -> {
                            request.setNotificationVisibility(1)
                        }
                        else -> {
                            request.setNotificationVisibility(0)
                        }
                    }
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType))
                    val dm = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    dm.enqueue(request)
                    Toast.makeText(activity, getString(R.string.downloading), Toast.LENGTH_LONG).show()
                }
            } else -> {
            webView.setDownloadListener { url, _, _, _, _ ->
                run {
                    startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    )}
                }
            }
        }

        // Pull refresh
        val cloudRefresh = requireActivity().findViewById<androidx.swiperefreshlayout.widget.SwipeRefreshLayout>(R.id.cloud_refresh)
        cloudRefresh.setOnRefreshListener {
            requireActivity().title = getString(R.string.cloud)
            webView.reload()
            cloudRefresh.isRefreshing = false
        }
    }
}