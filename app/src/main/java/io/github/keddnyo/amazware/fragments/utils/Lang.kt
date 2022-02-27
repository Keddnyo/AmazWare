package io.github.keddnyo.amazware.fragments.utils

import android.content.Context
import androidx.preference.PreferenceManager
import io.github.keddnyo.amazware.R

class Lang {
    fun rename(context: Context, lang: String): String {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context) // Shared Preferences

        val lang_zh = context.getString(R.string.lang_zh )
        val lang_en = context.getString(R.string.lang_en )
        val lang_es = context.getString(R.string.lang_es )
        val lang_ru = context.getString(R.string.lang_ru )
        val lang_fr = context.getString(R.string.lang_fr )
        val lang_de = context.getString(R.string.lang_de )
        val lang_it = context.getString(R.string.lang_it )
        val lang_tr = context.getString(R.string.lang_tr )
        val lang_pt_br = context.getString(R.string.lang_pt_br )
        val lang_pl = context.getString(R.string.lang_pl )
        val lang_ja = context.getString(R.string.lang_ja )
        val lang_ko = context.getString(R.string.lang_ko )
        val lang_th = context.getString(R.string.lang_th )
        val lang_id = context.getString(R.string.lang_id )
        val lang_ar = context.getString(R.string.lang_ar )
        val lang_he = context.getString(R.string.lang_he )
        val lang_vi = context.getString(R.string.lang_vi )
        val lang_uk = context.getString(R.string.lang_uk )
        val lang_cs = context.getString(R.string.lang_cs )
        val lang_el = context.getString(R.string.lang_el )
        val lang_sr = context.getString(R.string.lang_sr )
        val lang_sr_latn_rs = context.getString(R.string.lang_sr_latn_rs )
        val lang_ca_es = context.getString(R.string.lang_ca_es )
        val lang_fi = context.getString(R.string.lang_fi )
        val lang_no = context.getString(R.string.lang_no )
        val lang_da = context.getString(R.string.lang_da )
        val lang_sv = context.getString(R.string.lang_sv )
        val lang_pt = context.getString(R.string.lang_pt )
        val lang_ro = context.getString(R.string.lang_ro )
        val lang_ms = context.getString(R.string.lang_ms )
        val lang_in = context.getString(R.string.lang_in )
        val lang_ca = context.getString(R.string.lang_ca )
        val lang_iw = context.getString(R.string.lang_iw )
        val lang_nl = context.getString(R.string.lang_nl )

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
                l = l.replace("sr",". sr .")

                l = l.replace(". sr .-latn-rs","sr-latn-rs")

                l = l.replace("sr-latn-rs",". sr-latn-rs .")

                l = l.replace(". ca .-es","ca-es")

                l = l.replace("ca-es",". ca-es .")
                l = l.replace("fi",". fi .")
                l = l.replace("no",". no .")
                l = l.replace("da",". da .")
                l = l.replace("sv",". sv .")
                l = l.replace("ro",". ro .")
                l = l.replace("ms",". ms .")
                l = l.replace("in",". in .")
                l = l.replace("ca",". ca .")
                l = l.replace("iw",". iw .")
                l = l.replace("nl",". nl .")

                l = l.replace(". zh .",". $lang_zh .")
                l = l.replace(". en .",". $lang_en .")
                l = l.replace(". es .",". $lang_es .")
                l = l.replace(". ru .",". $lang_ru .")
                l = l.replace(". fr .",". $lang_fr .")
                l = l.replace(". de .",". $lang_de .")
                l = l.replace(". it .",". $lang_it .")
                l = l.replace(". tr .",". $lang_tr .")
                l = l.replace(". pt-br .",". $lang_pt_br .")
                l = l.replace(". pl .",". $lang_pl .")
                l = l.replace(". ja .",". $lang_ja .")
                l = l.replace(". ko .",". $lang_ko .")
                l = l.replace(". th .",". $lang_th .")
                l = l.replace(". id .",". $lang_id .")
                l = l.replace(". ar .",". $lang_ar .")
                l = l.replace(". he .",". $lang_he .")
                l = l.replace(". vi .",". $lang_vi .")
                l = l.replace(". uk .",". $lang_uk .")
                l = l.replace(". cs .",". $lang_cs .")
                l = l.replace(". el .",". $lang_el .")
                l = l.replace(". sr .",". $lang_sr .")
                l = l.replace(". sr-latn-rs .",". $lang_sr_latn_rs .")
                l = l.replace(". ca-es .",". $lang_ca_es .")
                l = l.replace(". fi .",". $lang_fi .")
                l = l.replace(". no .",". $lang_no .")
                l = l.replace(". da .",". $lang_da .")
                l = l.replace(". sv .",". $lang_sv .")
                l = l.replace(". pt .",". $lang_pt .")
                l = l.replace(". ro .",". $lang_ro .")
                l = l.replace(". ms .",". $lang_ms .")
                l = l.replace(". in .",". $lang_in .")
                l = l.replace(". ca .",". $lang_ca .")
                l = l.replace(". iw .",". $lang_iw .")
                l = l.replace(". nl .",". $lang_nl .")

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