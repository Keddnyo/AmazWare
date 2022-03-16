package io.github.keddnyo.amazware.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.preference.PreferenceManager
import io.github.keddnyo.amazware.R
import io.github.keddnyo.amazware.activities.ExtrasResponse
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*


class MakeRequest {

    fun getDevices(): Request {
        val url = "https://schakal.ru/fw/dev_apps.json"
        return Request.Builder().url(url).build()
    }

    fun getLatest(): Request {
        val url = "https://schakal.ru/fw/latest.json"
        return Request.Builder().url(url).build()
    }

    fun openMain(context: Context): String {
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

    fun openMainDevice(context: Context): String {
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

    fun firmwareRequest(context: Context, ps: String, ds: String, av: String, an: String) {
        val requestHost = "api-mifit-ru.huami.com"
        // val requestHost = "api-mifit-us2.huami.com"
        // val requestHost = "api.amazfit.com"

        val intent = Intent(context, ExtrasResponse::class.java)

        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, context.resources.getString(R.string.extras_gathering_info), Toast.LENGTH_SHORT).show()
        }

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

        val request = Request.Builder()
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

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                val json = JSONObject(response.body()!!.string())

                intent.putExtra("json", json.toString())

                if (json.has("firmwareVersion")) {
                    val firmwareVersion =
                        json.getString("firmwareVersion") // Firmware
                    val firmwareMd5 = json.getString("firmwareMd5") // Firmware MD5
                    val firmwareUrl = json.getString("firmwareUrl")

                    intent.putExtra("firmwareVersion", firmwareVersion)
                    intent.putExtra("firmwareMd5", firmwareMd5)
                    intent.putExtra("firmwareUrl", firmwareUrl)
                }
                if (json.has("resourceVersion")) {
                    val resourceVersion =
                        json.getString("resourceVersion") // Resources
                    val resourceMd5 = json.getString("resourceMd5") // Resources MD5
                    val resourceUrl = json.getString("resourceUrl")

                    intent.putExtra("resourceVersion", resourceVersion)
                    intent.putExtra("resourceMd5", resourceMd5)
                    intent.putExtra("resourceUrl", resourceUrl)
                }
                if (json.has("baseResourceVersion")) {
                    val baseResourceVersion =
                        json.getString("baseResourceVersion") // Base resources
                    val baseResourceMd5 =
                        json.getString("baseResourceMd5") // Base resources MD5
                    val baseResourceUrl = json.getString("baseResourceUrl")

                    intent.putExtra("baseResourceVersion", baseResourceVersion)
                    intent.putExtra("baseResourceMd5", baseResourceMd5)
                    intent.putExtra("baseResourceUrl", baseResourceUrl)
                }
                if (json.has("fontVersion")) {
                    val fontVersion = json.getString("fontVersion") // Font
                    val fontMd5 = json.getString("fontMd5") // Font MD5
                    val fontUrl = json.getString("fontUrl")

                    intent.putExtra("fontVersion", fontVersion)
                    intent.putExtra("fontMd5", fontMd5)
                    intent.putExtra("fontUrl", fontUrl)
                }
                if (json.has("gpsVersion")) {
                    val gpsVersion = json.getString("gpsVersion") // gpsVersion
                    val gpsMd5 = json.getString("gpsMd5") // gpsVersion
                    val gpsUrl = json.getString("gpsUrl")

                    intent.putExtra("gpsVersion", gpsVersion)
                    intent.putExtra("gpsMd5", gpsMd5)
                    intent.putExtra("gpsUrl", gpsUrl)
                }
                if (json.has("lang")) {
                    val lang = json.getString("lang") // Languages
                    intent.putExtra("lang", lang)
                }
                if (json.has("changeLog")) {
                    val changelog = json.getString("changeLog") // changeLog
                    intent.putExtra("changelog", changelog)
                }
                if (json.has("firmwareVersion")) {
                    (context as Activity).finish()
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } else {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, context.resources.getString(R.string.firmware_not_found), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}