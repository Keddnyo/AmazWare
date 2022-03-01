package io.github.keddnyo.amazware.utils

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.webkit.URLUtil
import android.widget.Toast
import androidx.preference.PreferenceManager
import io.github.keddnyo.amazware.R

class Download {
    fun run (context: Context, fileUrl: String) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context) // Shared Preferences
        when {
            sharedPreferences.getBoolean("download_provider", true) -> {
                val request = DownloadManager.Request(Uri.parse(fileUrl))
                request.setDescription(context.getString(R.string.downloading))
                request.setTitle(URLUtil.guessFileName(fileUrl, "?", "?"))
                request.allowScanningByMediaScanner()
                when {
                    sharedPreferences.getBoolean("download_notification", true) -> {
                        request.setNotificationVisibility(1)
                    }
                    else -> {
                        request.setNotificationVisibility(0)
                    }
                }
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(fileUrl, "?", "?"))
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