package io.github.keddnyo.amazware.utils

import android.content.Context
import android.net.Uri
import androidx.preference.PreferenceManager
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class MakeRequest {
    fun openFirmwarehouse(context: Context): String {
        val lang = Locale.getDefault().language.toString()
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context) // Shared Preferences
        val url: Uri.Builder = Uri.Builder()
        val theme = when (sharedPreferences.getBoolean("dark_mode", false)) { // Set theme
            true -> {
                "dark"
            }
            false -> {
                "light"
            }
        }

        url.scheme("https")
            .authority("schakal.ru")
            .appendPath("fw")
            .appendPath("firmwares_list.htm")
            .appendQueryParameter("theme", theme)
            .appendQueryParameter("lang", lang)

        return url.toString()
    }
    fun openFirmwarehouseDevice(context: Context): String {
        val lang = Locale.getDefault().language.toString()
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context) // Shared Preferences
        val deviceSource = sharedPreferences.getString("deviceSource", "") // Device from Shared Preferences (saved)
        val url: Uri.Builder = Uri.Builder()
        val theme = when (sharedPreferences.getBoolean("dark_mode", false)) { // Set theme
            true -> {
                "dark"
            }
            false -> {
                "light"
            }
        }

        url.scheme("https")
            .authority("schakal.ru")
            .appendPath("fw")
            .appendPath("firmwares_list.htm")
            .appendQueryParameter("theme", theme)
            .appendQueryParameter("lang", lang)
            .appendQueryParameter("device", deviceSource)

        return url.toString()
    }

    fun directDevice(ps: String, ds: String, av: String, an: String): Request {
        val requestHost = "api-mifit-ru.huami.com"
        // val requestHost = "api-mifit-us2.huami.com"
        // val requestHost = "api.amazfit.com"

        val uriBuilder: Uri.Builder = Uri.Builder()
        uriBuilder.scheme("https")
            .authority(requestHost)
            .appendPath("devices")
            .appendPath("ALL")
            .appendPath("hasNewVersion")
            .appendQueryParameter("productId", "0")
            .appendQueryParameter("vendorSource", "0")
            .appendQueryParameter("resourceVersion", "0")
            .appendQueryParameter("firmwareFlag", "0")
            .appendQueryParameter("vendorId", "0")
            .appendQueryParameter("resourceFlag", "0")
            .appendQueryParameter("productionSource", ps)
            .appendQueryParameter("userid", "0")
            .appendQueryParameter("userId", "0")
            .appendQueryParameter("deviceSource", ds)
            .appendQueryParameter("fontVersion", "0")
            .appendQueryParameter("fontFlag", "3")
            .appendQueryParameter("appVersion", av)
            .appendQueryParameter("appid", "0")
            .appendQueryParameter("callid", "0")
            .appendQueryParameter("channel", "0")
            .appendQueryParameter("country", "0")
            .appendQueryParameter("cv", "0")
            .appendQueryParameter("device", "0")
            .appendQueryParameter("deviceType", "ALL")
            .appendQueryParameter("device_type", "android_phone")
            .appendQueryParameter("firmwareVersion", "0")
            .appendQueryParameter("hardwareVersion", "0")
            .appendQueryParameter("lang", "0")
            .appendQueryParameter("support8Bytes", "true")
            .appendQueryParameter("timezone", "0")
            .appendQueryParameter("v", "0")
            .appendQueryParameter("gpsVersion", "0")
            .appendQueryParameter("baseResourceVersion", "0")

        return Request.Builder()
            .url(uriBuilder.toString())
            .addHeader("hm-privacy-diagnostics", "false")
            .addHeader("country", "0")
            .addHeader("appplatform", "android_phone")
            .addHeader("hm-privacy-ceip", "0")
            .addHeader("x-request-id", "0")
            .addHeader("timezone", "0")
            .addHeader("channel", "0")
            .addHeader("user-agent", "0")
            .addHeader("cv", "0")
            .addHeader("appname", an)
            .addHeader("v", "0")
            .addHeader("apptoken", "0")
            .addHeader("lang", "0")
            .addHeader("Host", "api-mifit-ru.huami.com")
            .addHeader("Connection", "Keep-Alive")
            .addHeader("accept-encoding", "gzip")
            .addHeader("accept", "*/*")
            .build()
    }

    fun getDevices(): Request {
        val url = "https://schakal.ru/fw/dev_apps.json"
        return Request.Builder().url(url).build()
    }

    fun getLatest(): Request {
        val url = "https://schakal.ru/fw/latest.json"
        return Request.Builder().url(url).build()
    }
}