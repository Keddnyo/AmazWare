package io.github.keddnyo.amazware.fragments.utils

import android.content.Context
import androidx.preference.PreferenceManager

class Lang {
    fun rename(context: Context, lang: String): String {
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