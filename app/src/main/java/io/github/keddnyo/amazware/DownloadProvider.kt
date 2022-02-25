package io.github.keddnyo.amazware

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.webkit.URLUtil
import android.widget.Toast
import androidx.preference.PreferenceManager

class DownloadProvider {
    fun download (context: Context, fileUrl: String, contentDisposition: String, mimeType: String) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context) // Shared Preferences

        // Downloading code
        when {
            sharedPreferences.getBoolean("download_provider", true) -> {
                val request = DownloadManager.Request(Uri.parse(fileUrl))

                request.setDescription(context.getString(R.string.downloading))
                request.setTitle(URLUtil.guessFileName(fileUrl, contentDisposition, mimeType))
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
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(fileUrl, contentDisposition, mimeType))
                val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                dm.enqueue(request)
                Toast.makeText(context, context.getString(R.string.downloading), Toast.LENGTH_LONG).show()
            } else -> {
            run {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl))
                )}
            }
        }
    }
}