package io.github.keddnyo.amazware.utils

import android.content.Context
import android.net.Uri
import android.widget.EditText
import androidx.preference.PreferenceManager
import okhttp3.Request

class Extras {

    fun deviceRequest(ps: EditText, ds: EditText, av: EditText, an: EditText): Request {
        val requestHost = "api-mifit-ru.huami.com"

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
            .appendQueryParameter("productionSource", "${ps.text}")
            .appendQueryParameter("userid", "0")
            .appendQueryParameter("userId", "0")
            .appendQueryParameter("deviceSource", "${ds.text}")
            .appendQueryParameter("fontVersion", "0")
            .appendQueryParameter("fontFlag", "3")
            .appendQueryParameter("appVersion", "${av.text}")
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
            .addHeader("appname", "${an.text}")
            .addHeader("v", "0")
            .addHeader("apptoken", "0")
            .addHeader("lang", "0")
            .addHeader("Host", "api-mifit-ru.huami.com")
            .addHeader("Connection", "Keep-Alive")
            .addHeader("accept-encoding", "gzip")
            .addHeader("accept", "*/*")
            .build()
    }

    fun renameLang(context: Context, lang: String): String {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context) // Shared Preferences
        return when (sharedPreferences.getBoolean("lang_formatted", true)) {
            true -> {
                var l: String = lang.replace(",", " , ")

                l = l.replace("zh",". zh .")
                l = l.replace("en",". en .")
                l = l.replace("es",". es .")
                l = l.replace("ru",". ru .")
                l = l.replace("fr",". fr .")
                l = l.replace("de",". de .")
                l = l.replace("it",". it .")
                l = l.replace("tr",". tr .")
                l = l.replace("pt",". pt .")

                l = l.replace(". pt .-br","pt-br")

                l = l.replace("pt-br",". pt-br .")
                l = l.replace("pl",". pl .")
                l = l.replace("ja",". ja .")
                l = l.replace("ko",". ko .")
                l = l.replace("th",". th .")
                l = l.replace("id",". id .")
                l = l.replace("ar",". ar .")
                l = l.replace("he",". he .")
                l = l.replace("vi",". vi .")
                l = l.replace("uk",". uk .")
                l = l.replace("cs",". cs .")
                l = l.replace("el",". el .")
                l = l.replace("sr-latn-rs",". sr-latn-rs .")
                l = l.replace("ca-es",". ca-es .")
                l = l.replace("fi",". fi .")
                l = l.replace("no",". no .")
                l = l.replace("da",". da .")
                l = l.replace("sv",". sv .")
                l = l.replace("ro",". ro .")
                l = l.replace("ms",". ms .")
                l = l.replace("in",". in .")
                l = l.replace("sr",". sr .")
                l = l.replace("ca",". ca .")
                l = l.replace("iw",". iw .")

                l = l.replace(". zh .",". Chinese .")
                l = l.replace(". en .",". English .")
                l = l.replace(". es .",". Spanish .")
                l = l.replace(". ru .",". Russian .")
                l = l.replace(". fr .",". French .")
                l = l.replace(". de .",". German .")
                l = l.replace(". it .",". Italian .")
                l = l.replace(". tr .",". Turkish .")
                l = l.replace(". pt-br .",". Brazilian Portuguese .")
                l = l.replace(". pl .",". Polish .")
                l = l.replace(". ja .",". Japanese .")
                l = l.replace(". ko .",". Korean .")
                l = l.replace(". th .",". Thai .")
                l = l.replace(". id .",". Indonesian .")
                l = l.replace(". ar .",". Arabic .")
                l = l.replace(". he .",". Hebrew .")
                l = l.replace(". vi .",". Vietnamese .")
                l = l.replace(". uk .",". Ukrainian .")
                l = l.replace(". cs .",". Czech .")
                l = l.replace(". el .",". Greek .")
                l = l.replace(". sr-latn-rs .",". Serbian .")
                l = l.replace(". ca-es .",". Catalan .")
                l = l.replace(". fi .",". Finnish .")
                l = l.replace(". no .",". Norwegian .")
                l = l.replace(". da .",". Danish .")
                l = l.replace(". sv .",". Swedish .")
                l = l.replace(". pt .",". Portuguese .")
                l = l.replace(". ro .",". Romanian .")
                l = l.replace(". ms .",". Malay .")
                l = l.replace(". in .",". Indonesian .")
                l = l.replace(". sr .",". Serbian .")
                l = l.replace(". ca .",". Catalan .")
                l = l.replace(". iw .",". former Hebrew .")

                l = l.replace(". ","")
                l = l.replace(" .","")
                l.replace(" , ",", ")
            }
            false -> {
                lang.replace(",", ", ")
            }
        }
    }
}