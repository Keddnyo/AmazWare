package io.github.keddnyo.amazware.utils

import android.content.Context
import io.github.keddnyo.amazware.R

class Lang {
    fun rename(context: Context, lang: String): String {
        val langZH = context.getString(R.string.lang_zh)
        val langEN = context.getString(R.string.lang_en)
        val langES = context.getString(R.string.lang_es)
        val langRU = context.getString(R.string.lang_ru)
        val langFR = context.getString(R.string.lang_fr)
        val langDE = context.getString(R.string.lang_de)
        val langIT = context.getString(R.string.lang_it)
        val langTR = context.getString(R.string.lang_tr)
        val langPTBR = context.getString(R.string.lang_pt_br)
        val langPL = context.getString(R.string.lang_pl)
        val langJA = context.getString(R.string.lang_ja)
        val langKO = context.getString(R.string.lang_ko)
        val langTH = context.getString(R.string.lang_th)
        val langID = context.getString(R.string.lang_id)
        val langAR = context.getString(R.string.lang_ar)
        val langHE = context.getString(R.string.lang_he)
        val langVI = context.getString(R.string.lang_vi)
        val langUK = context.getString(R.string.lang_uk)
        val langCS = context.getString(R.string.lang_cs)
        val langEL = context.getString(R.string.lang_el)
        val langSR = context.getString(R.string.lang_sr)
        val langSRLATNRS = context.getString(R.string.lang_sr_latn_rs)
        val langCAES = context.getString(R.string.lang_ca_es)
        val langFI = context.getString(R.string.lang_fi)
        val langNO = context.getString(R.string.lang_no)
        val langDA = context.getString(R.string.lang_da)
        val langSV = context.getString(R.string.lang_sv)
        val langPT = context.getString(R.string.lang_pt)
        val langRO = context.getString(R.string.lang_ro)
        val langMS = context.getString(R.string.lang_ms)
        val langIN = context.getString(R.string.lang_in)
        val langCA = context.getString(R.string.lang_ca)
        val langIW = context.getString(R.string.lang_iw)
        val langNL = context.getString(R.string.lang_nl)

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

        l = l.replace(". pt .-br",". pt-br .") // For schakal host output
        l = l.replace(". pt ._br",". pt_br .") // For Xiaomi server output

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

        l = l.replace(". sr .-latn-rs",". sr-latn-rs .")

        l = l.replace(". ca .-es",". ca-es .")

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

        l = l.replace(". zh .",". $langZH .")
        l = l.replace(". en .",". $langEN .")
        l = l.replace(". es .",". $langES .")
        l = l.replace(". ru .",". $langRU .")
        l = l.replace(". fr .",". $langFR .")
        l = l.replace(". de .",". $langDE .")
        l = l.replace(". it .",". $langIT .")
        l = l.replace(". tr .",". $langTR .")
        l = l.replace(". pt-br .",". $langPTBR .")
        l = l.replace(". pt_br .",". $langPTBR .")
        l = l.replace(". pl .",". $langPL .")
        l = l.replace(". ja .",". $langJA .")
        l = l.replace(". ko .",". $langKO .")
        l = l.replace(". th .",". $langTH .")
        l = l.replace(". id .",". $langID .")
        l = l.replace(". ar .",". $langAR .")
        l = l.replace(". he .",". $langHE .")
        l = l.replace(". vi .",". $langVI .")
        l = l.replace(". uk .",". $langUK .")
        l = l.replace(". cs .",". $langCS .")
        l = l.replace(". el .",". $langEL .")
        l = l.replace(". sr .",". $langSR .")
        l = l.replace(". sr-latn-rs .",". $langSRLATNRS .")
        l = l.replace(". ca-es .",". $langCAES .")
        l = l.replace(". fi .",". $langFI .")
        l = l.replace(". no .",". $langNO .")
        l = l.replace(". da .",". $langDA .")
        l = l.replace(". sv .",". $langSV .")
        l = l.replace(". pt .",". $langPT .")
        l = l.replace(". ro .",". $langRO .")
        l = l.replace(". ms .",". $langMS .")
        l = l.replace(". in .",". $langIN .")
        l = l.replace(". ca .",". $langCA .")
        l = l.replace(". iw .",". $langIW .")
        l = l.replace(". nl .",". $langNL .")

        l = l.replace(". ","")
        l = l.replace(" .","")
        return l.replace(" , ",", ")
    }
}