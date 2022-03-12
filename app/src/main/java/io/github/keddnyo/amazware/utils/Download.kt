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
    fun run(c: Context, fileUrl: String) {
        val pm = PreferenceManager.getDefaultSharedPreferences(c)

        when {
            pm.getBoolean("download_provider", true) -> {
                val request = DownloadManager.Request(Uri.parse(fileUrl))
                val fileName = URLUtil.guessFileName(fileUrl, "?", "?")
                request.setTitle(fileName)
                request.setDescription(c.getString(R.string.app_name))
                request.setNotificationVisibility(1)
                request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    fileName
                )
                val dm = c.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                dm.enqueue(request)
                Toast.makeText(c, c.getString(R.string.downloading), Toast.LENGTH_LONG).show()
            }
            else -> {
                c.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl))
                )
            }
        }
    }
}