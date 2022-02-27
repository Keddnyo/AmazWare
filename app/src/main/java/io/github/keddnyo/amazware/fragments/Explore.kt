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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.github.keddnyo.amazware.fragments.utils.Download
import io.github.keddnyo.amazware.R
import io.github.keddnyo.amazware.fragments.utils.MakeRequest

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

        val webView = requireActivity().findViewById<WebView>(R.id.webView)
        val webSettings = webView?.settings
        val refresh =
            requireActivity().findViewById<SwipeRefreshLayout>(R.id.cloud_refresh)

        webSettings!!.javaScriptEnabled = true
        webView.loadUrl(MakeRequest().openExplorePage(requireContext())) // Loading WebView
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
                requireActivity().title = getString(R.string.error)
            }
        }

        webView.setDownloadListener { fileUrl, _, contentDisposition, mimeType, _ -> // Downloading code
            context?.let { Download().run(it, fileUrl, contentDisposition, mimeType) }
        }

        refresh.setOnRefreshListener { // Pull refresh
            requireActivity().title = getString(R.string.explore)
            webView.reload()
            refresh.isRefreshing = false
        }
    }
}