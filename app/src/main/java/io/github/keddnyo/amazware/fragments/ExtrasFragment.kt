package io.github.keddnyo.amazware.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import io.github.keddnyo.amazware.Adapter
import io.github.keddnyo.amazware.DownloadProvider
import io.github.keddnyo.amazware.R
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ExtrasFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_extras, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = getString(R.string.extras) // New Title

        // Variables
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity()) // Shared Preferences

        val deviceSource =  requireActivity().findViewById<EditText>(R.id.deviceSource)
        val productionSource =  requireActivity().findViewById<EditText>(R.id.productionSource)
        val appName =  requireActivity().findViewById<EditText>(R.id.appName)
        val appVersion =  requireActivity().findViewById<EditText>(R.id.appVersion)
        val channelPlay =  requireActivity().findViewById<CheckBox>(R.id.channelPlay)
        val buttonReset =  requireActivity().findViewById<Button>(R.id.buttonReset)
        val buttonSubmit =  requireActivity().findViewById<Button>(R.id.buttonSubmit)
        val deviceSpinner = requireActivity().findViewById<Spinner>(R.id.deviceList)
        val responseList = requireActivity().findViewById<ListView>(R.id.responseList)
        val responseField = requireActivity().findViewById<TextView>(R.id.responseField)

        appVersion.addTextChangedListener (object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                buttonSubmit.isEnabled = deviceSource.text.isNotEmpty() && productionSource.text.isNotEmpty() && appName.text.isNotEmpty() && appVersion.text.isNotEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        val devList = ArrayList<String>()
        val devAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, devList)

        val okHttpClient = OkHttpClient()
        val urlMain = "https://schakal.ru/fw/dev_apps.json"
        val requestMain = Request.Builder().url(urlMain).build()

        deviceSpinner.post{
            devList.add(getString(R.string.manual_input))
            devAdapter.notifyDataSetChanged()
        }

        okHttpClient.newCall(requestMain).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                val json = JSONObject(response.body()!!.string())

                val output = json.toString()
                output.replace(",", ", ")
                output.substringBefore('#')
                for (i in 1..1000) {
                    if (json.has(i.toString())) {
                        deviceSpinner.post{
                            val name = json.getJSONObject(i.toString()).getString("name")
                            devList.add(name)
                            devAdapter.notifyDataSetChanged()
                        }
                    }
                }

                deviceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                        responseList.visibility = View.GONE

                        val selectedItem = deviceSpinner.selectedItem.toString()

                        for (i in 1..1000) {
                            if (json.has(i.toString())) { // Existing indexes
                                val nameResult = json.getJSONObject(i.toString()).getString("name")
                                val productionSourceResult = json.getJSONObject(i.toString()).getString("productionSource")
                                val appnameResult = json.getJSONObject(i.toString()).getString("appname")
                                val appVersionResult = json.getJSONObject(i.toString()).getString("appVersion")

                                if (selectedItem == nameResult) { // Compare values
                                    deviceSource.setText(i.toString())
                                    productionSource.setText(productionSourceResult)
                                    appName.setText(appnameResult)
                                    appVersion.setText(appVersionResult)

                                    deviceSource.isEnabled = false
                                    productionSource.isEnabled = false
                                } else if (selectedItem == getString(R.string.manual_input)) {
                                    deviceSource.isEnabled = true
                                    productionSource.isEnabled = true
                                    buttonSubmit.isEnabled
                                }
                            }
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        })
        deviceSpinner.adapter = devAdapter

        // Setting response adapter
        val list = ArrayList<Adapter>()
        val adapter = SimpleAdapter(
            activity,
            list,
            android.R.layout.two_line_list_item,
            arrayOf(Adapter.NAME, Adapter.DESCRIPTION),
            intArrayOf(
                android.R.id.text1, android.R.id.text2
            )
        )
        responseList.adapter = adapter
        // Setting response adapter

        buttonReset.setOnClickListener {
            // Clear views

            channelPlay.isEnabled = false
            channelPlay.visibility = View.GONE

            deviceSource.setText("")
            productionSource.setText("")
            appName.setText("")
            appVersion.setText("")

            responseList.visibility = View.GONE

            list.clear()
            responseField.visibility = View.GONE
            responseField.text = ""

            deviceSource.requestFocus()

            deviceSource.isEnabled = true
            productionSource.isEnabled = true

            deviceSpinner.setSelection(0)
            buttonSubmit.isEnabled = false
        }

        buttonSubmit.setOnClickListener {
            // Clear for a new list
            list.clear()
            responseField.visibility = View.GONE
            responseField.text = ""

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
                .appendQueryParameter("productionSource", productionSource.text.toString())
                .appendQueryParameter("userid", "0")
                .appendQueryParameter("userId", "0")
                .appendQueryParameter("deviceSource", deviceSource.text.toString())
                .appendQueryParameter("fontVersion", "0")
                .appendQueryParameter("fontFlag", "3")
                .appendQueryParameter("appVersion", appVersion.text.toString())
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
                .addHeader("appname", appName.text.toString())
                .addHeader("v", "0")
                .addHeader("apptoken", "0")
                .addHeader("lang", "0")
                .addHeader("Host", "api-mifit-ru.huami.com")
                .addHeader("Connection", "Keep-Alive")
                .addHeader("accept-encoding", "gzip")
                .addHeader("accept", "*/*")

                .build()

            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    requireActivity().title = getString(R.string.error)
                }

                override fun onResponse(call: Call, response: Response) {
                    val json = JSONObject(response.body()!!.string())

                    val output = json.toString()
                    output.replace(",", ", ")
                    output.substringBefore('#')

                    val none = getString(R.string.none)

                    when (sharedPreferences.getBoolean("simple_response", true)) {
                        false -> {
                            responseField.post {
                                responseField.visibility = View.VISIBLE
                                responseField.text = json.toString()
                            }
                        }
                        true -> {
                            responseList.post {
                                responseList.visibility = View.VISIBLE

                                if (json.has("firmwareVersion")) {
                                    val firmwareVersion = json.getString("firmwareVersion") // Firmware
                                    val firmwareMd5 = json.getString("firmwareMd5") // Firmware MD5
                                    list.add(
                                        Adapter(
                                            getString(R.string.firmwareVersion) + ": " + firmwareVersion,
                                            "MD5: $firmwareMd5"
                                        )
                                    )
                                    adapter.notifyDataSetChanged() // Commit changes
                                } else {
                                    list.add(
                                        Adapter(
                                            getString(R.string.firmwareVersion) + ": " + none,
                                            "MD5: $none"
                                        )
                                    )
                                    adapter.notifyDataSetChanged() // Commit changes
                                }
                                if (json.has("resourceVersion")) {
                                    val resourceVersion = json.getString("resourceVersion") // Resources
                                    val resourceMd5 = json.getString("resourceMd5") // Resources MD5
                                    list.add(
                                        Adapter(
                                            getString(R.string.resourceVersion) + ": " + resourceVersion,
                                            "MD5: $resourceMd5"
                                        )
                                    )
                                    adapter.notifyDataSetChanged() // Commit changes
                                } else {
                                    list.add(
                                        Adapter(
                                            getString(R.string.resourceVersion) + ": " + none,
                                            "MD5: $none"
                                        )
                                    )
                                    adapter.notifyDataSetChanged() // Commit changes
                                }
                                if (json.has("baseResourceVersion")) {
                                    val baseResourceVersion = json.getString("baseResourceVersion") // Base resources
                                    val baseResourceMd5 = json.getString("baseResourceMd5") // Base resources MD5
                                    list.add(
                                        Adapter(
                                            getString(R.string.baseResourceVersion) + ": " + baseResourceVersion,
                                            "MD5: $baseResourceMd5"
                                        )
                                    )
                                    adapter.notifyDataSetChanged() // Commit changes
                                } else {
                                    list.add(
                                        Adapter(
                                            getString(R.string.baseResourceVersion) + ": " + none,
                                            "MD5: $none"
                                        )
                                    )
                                    adapter.notifyDataSetChanged() // Commit changes
                                }
                                if (json.has("fontVersion")) {
                                    val fontVersion = json.getString("fontVersion") // Font
                                    val fontMd5 = json.getString("fontMd5") // Font MD5
                                    list.add(
                                        Adapter(
                                            getString(R.string.fontVersion) + ": " + fontVersion,
                                            "MD5: $fontMd5"
                                        )
                                    )
                                    adapter.notifyDataSetChanged() // Commit changes
                                } else {
                                    list.add(
                                        Adapter(
                                            getString(R.string.fontVersion) + ": " + none,
                                            "MD5: $none"
                                        )
                                    )
                                    adapter.notifyDataSetChanged() // Commit changes
                                }
                                if (json.has("gpsVersion")) {
                                    val gpsVersion = json.getString("gpsVersion") // gpsVersion
                                    val gpsMd5 = json.getString("gpsMd5") // gpsVersion
                                    list.add(
                                        Adapter(
                                            getString(R.string.gpsVersion) + ": " + gpsVersion,
                                            "MD5: $gpsMd5"
                                        )
                                    )
                                    adapter.notifyDataSetChanged() // Commit changes
                                } else {
                                    list.add(
                                        Adapter(
                                            getString(R.string.gpsVersion) + ": " + none,
                                            "MD5: $none"
                                        )
                                    )
                                    adapter.notifyDataSetChanged() // Commit changes
                                }
                                if (json.has("lang")) {
                                    val lang = json.getString("lang") // Languages
                                    val language = replace(requireActivity(), lang)
                                    list.add(
                                        Adapter(
                                            getString(R.string.lang),
                                            language
                                        )
                                    )
                                    adapter.notifyDataSetChanged() // Commit changes
                                } else {
                                    list.add(
                                        Adapter(
                                            getString(R.string.lang),
                                            none
                                        )
                                    )
                                    adapter.notifyDataSetChanged() // Commit changes
                                }
                                if (json.has("changeLog")) {
                                    var changelog = json.getString("changeLog") // changeLog
                                    changelog = changelog.substringBefore('#')
                                    list.add(
                                        Adapter(
                                            getString(R.string.changeLog),
                                            changelog
                                        )
                                    )
                                    adapter.notifyDataSetChanged() // Commit changes
                                }
                                if (!json.has("firmwareVersion")) {
                                    responseField.post {
                                        responseList.visibility = View.GONE
                                        responseField.visibility = View.VISIBLE
                                        responseField.text = json.toString()
                                    }
                                    Toast.makeText(context, getString(R.string.firmware_not_found), Toast.LENGTH_SHORT).show()
                                }
                            }

                            responseList.onItemClickListener =
                                OnItemClickListener { _, _, position, _ ->
                                    when (position) {
                                        0 -> {
                                            if (json.has("firmwareUrl")) {
                                                val fileUrl = json.getString("firmwareUrl")
                                                context?.let { DownloadProvider().download(it, fileUrl, "firmware", "?") }
                                            } else {
                                                Toast.makeText(context, R.string.none, Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        1 -> {
                                            if (json.has("resourceUrl")) {
                                                val fileUrl = json.getString("resourceUrl")
                                                context?.let { DownloadProvider().download(it, fileUrl, "resource", "?") }
                                            } else {
                                                Toast.makeText(context, R.string.none, Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        2 -> {
                                            if (json.has("baseResourceUrl")) {
                                                val fileUrl = json.getString("baseResourceUrl")
                                                context?.let { DownloadProvider().download(it, fileUrl, "base_resource", "?") }
                                            } else {
                                                Toast.makeText(context, R.string.none, Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        3 -> {
                                            if (json.has("fontUrl")) {
                                                val fileUrl = json.getString("fontUrl")
                                                context?.let { DownloadProvider().download(it, fileUrl, "font", "?") }
                                            } else {
                                                Toast.makeText(context, R.string.none, Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        4 -> {
                                            if (json.has("gpsUrl")) {
                                                val fileUrl = json.getString("gpsUrl")
                                                context?.let { DownloadProvider().download(it, fileUrl, "gps", "?") }
                                            } else {
                                                Toast.makeText(context, R.string.none, Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        5 -> {
                                            if (json.has("lang")) {
                                                val lang = json.getString("lang")
                                                Toast.makeText(context, lang, Toast.LENGTH_SHORT).show()
                                            } else {
                                                Toast.makeText(context, R.string.none, Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                }
                        }
                    }
                }
            })
        }
    }

    fun replace(context: Activity, lang: String): String {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context) // Shared Preferences
        when (sharedPreferences.getBoolean("lang_formatted", true)) {
            true -> {
                var language: String = lang.replace(",", " , ")
                language = language.replace(" ab",". ab .")
                language = language.replace("aa",". aa .")
                language = language.replace("af",". af .")
                language = language.replace("ak",". ak .")
                language = language.replace("sq",". sq .")
                language = language.replace("am",". am .")
                language = language.replace("ar",". ar .")
                language = language.replace("an",". an .")
                language = language.replace("hy",". hy .")
                language = language.replace("as",". as .")
                language = language.replace("av",". av .")
                language = language.replace("ae",". ae .")
                language = language.replace("ay",". ay .")
                language = language.replace("az",". az .")
                language = language.replace("ba",". ba .")
                language = language.replace("eu",". eu .")
                language = language.replace("be",". be .")
                language = language.replace("bn",". bn .")
                language = language.replace("bh",". bh .")
                language = language.replace("bi",". bi .")
                language = language.replace("bm",". bm .")
                language = language.replace("bs",". bs .")
                language = language.replace("br",". br .")
                language = language.replace("bg",". bg .")
                language = language.replace("my",". my .")
                language = language.replace("ca",". ca .")
                language = language.replace("ch",". ch .")
                language = language.replace("ce",". ce .")
                language = language.replace("ny",". ny .")
                language = language.replace("zh",". zh .")
                language = language.replace("cv",". cv .")
                language = language.replace("kw",". kw .")
                language = language.replace("co",". co .")
                language = language.replace("cr",". cr .")
                language = language.replace("hr",". hr .")
                language = language.replace("cs",". cs .")
                language = language.replace("da",". da .")
                language = language.replace("dv",". dv .")
                language = language.replace("nl",". nl .")
                language = language.replace("en",". en .")
                language = language.replace("eo",". eo .")
                language = language.replace("et",". et .")
                language = language.replace("ee",". ee .")
                language = language.replace("fo",". fo .")
                language = language.replace("fj",". fj .")
                language = language.replace("fi",". fi .")
                language = language.replace("fr",". fr .")
                language = language.replace("ff",". ff .")
                language = language.replace("gl",". gl .")
                language = language.replace("ka",". ka .")
                language = language.replace("de",". de .")
                language = language.replace("el",". el .")
                language = language.replace("gn",". gn .")
                language = language.replace("gu",". gu .")
                language = language.replace("ht",". ht .")
                language = language.replace("ha",". ha .")
                language = language.replace("he",". he .")
                language = language.replace("hz",". hz .")
                language = language.replace("hi",". hi .")
                language = language.replace("ho",". ho .")
                language = language.replace("hu",". hu .")
                language = language.replace("ia",". ia .")
                language = language.replace("id",". id .")
                language = language.replace("ie",". ie .")
                language = language.replace("ga",". ga .")
                language = language.replace("ig",". ig .")
                language = language.replace("ik",". ik .")
                language = language.replace("io",". io .")
                language = language.replace("is",". is .")
                language = language.replace("it",". it .")
                language = language.replace("iu",". iu .")
                language = language.replace("ja",". ja .")
                language = language.replace("jv",". jv .")
                language = language.replace("kl",". kl .")
                language = language.replace("kn",". kn .")
                language = language.replace("kr",". kr .")
                language = language.replace("ks",". ks .")
                language = language.replace("kk",". kk .")
                language = language.replace("km",". km .")
                language = language.replace("ki",". ki .")
                language = language.replace("rw",". rw .")
                language = language.replace("ky",". ky .")
                language = language.replace("kv",". kv .")
                language = language.replace("kg",". kg .")
                language = language.replace("ko",". ko .")
                language = language.replace("ku",". ku .")
                language = language.replace("kj",". kj .")
                language = language.replace("la",". la .")
                language = language.replace("lb",". lb .")
                language = language.replace("lg",". lg .")
                language = language.replace("li",". li .")
                language = language.replace("ln",". ln .")
                language = language.replace("lo",". lo .")
                language = language.replace("lt",". lt .")
                language = language.replace("lu",". lu .")
                language = language.replace("lv",". lv .")
                language = language.replace("gv",". gv .")
                language = language.replace("mk",". mk .")
                language = language.replace("mg",". mg .")
                language = language.replace("ms",". ms .")
                language = language.replace("ml",". ml .")
                language = language.replace("mt",". mt .")
                language = language.replace("mi",". mi .")
                language = language.replace("mr",". mr .")
                language = language.replace("mh",". mh .")
                language = language.replace("mn",". mn .")
                language = language.replace("na",". na .")
                language = language.replace("nv",". nv .")
                language = language.replace("nb",". nb .")
                language = language.replace("nd",". nd .")
                language = language.replace("ne",". ne .")
                language = language.replace("ng",". ng .")
                language = language.replace("nn",". nn .")
                language = language.replace("no",". no .")
                language = language.replace("ii",". ii .")
                language = language.replace("nr",". nr .")
                language = language.replace("oc",". oc .")
                language = language.replace("oj",". oj .")
                language = language.replace("cu",". cu .")
                language = language.replace("om",". om .")
                language = language.replace("or",". or .")
                language = language.replace("os",". os .")
                language = language.replace("pa",". pa .")
                language = language.replace("pi",". pi .")
                language = language.replace("fa",". fa .")
                language = language.replace("pl",". pl .")
                language = language.replace("ps",". ps .")
                language = language.replace("pt",". pt .")
                language = language.replace("qu",". qu .")
                language = language.replace("rm",". rm .")
                language = language.replace("rn",". rn .")
                language = language.replace("ro",". ro .")
                language = language.replace("ru",". ru .")
                language = language.replace("sa",". sa .")
                language = language.replace("sc",". sc .")
                language = language.replace("sc",". sc .")
                language = language.replace("se",". se .")
                language = language.replace("sm",". sm .")
                language = language.replace("sg",". sg .")
                language = language.replace("sr",". sr .")
                language = language.replace("gd",". gd .")
                language = language.replace("sn",". sn .")
                language = language.replace("si",". si .")
                language = language.replace("sk",". sk .")
                language = language.replace("sl",". sl .")
                language = language.replace("so",". so .")
                language = language.replace("st",". st .")
                language = language.replace("es",". es .")
                language = language.replace("su",". su .")
                language = language.replace("sw",". sw .")
                language = language.replace("ss",". ss .")
                language = language.replace("sv",". sv .")
                language = language.replace("ta",". ta .")
                language = language.replace("te",". te .")
                language = language.replace("tg",". tg .")
                language = language.replace("th",". th .")
                language = language.replace("ti",". ti .")
                language = language.replace("bo",". bo .")
                language = language.replace("tk",". tk .")
                language = language.replace("tl",". tl .")
                language = language.replace("tn",". tn .")
                language = language.replace("to",". to .")
                language = language.replace("tr",". tr .")
                language = language.replace("ts",". ts .")
                language = language.replace("tt",". tt .")
                language = language.replace("tw",". tw .")
                language = language.replace("ty",". ty .")
                language = language.replace("ug",". ug .")
                language = language.replace("uk",". uk .")
                language = language.replace("ur",". ur .")
                language = language.replace("uz",". uz .")
                language = language.replace("ve",". ve .")
                language = language.replace("vi",". vi .")
                language = language.replace("vo",". vo .")
                language = language.replace("wa",". wa .")
                language = language.replace("cy",". cy .")
                language = language.replace("wo",". wo .")
                language = language.replace("fy",". fy .")
                language = language.replace("xh",". xh .")
                language = language.replace("yi",". yi .")
                language = language.replace("yo",". yo .")
                language = language.replace("za",". za .")


                language = language.replace(". ab .",". Abkhaz .")
                language = language.replace(". aa .",". Afar .")
                language = language.replace(". af .",". Afrikaans .")
                language = language.replace(". ak .",". Akan .")
                language = language.replace(". sq .",". Albanian .")
                language = language.replace(". am .",". Amharic .")
                language = language.replace(". ar .",". Arabic .")
                language = language.replace(". an .",". Aragonese .")
                language = language.replace(". hy .",". Armenian .")
                language = language.replace(". as .",". Assamese .")
                language = language.replace(". av .",". Avaric .")
                language = language.replace(". ae .",". Avestan .")
                language = language.replace(". ay .",". Aymara .")
                language = language.replace(". az .",". Azerbaijani .")
                language = language.replace(". ba .",". Bashkir .")
                language = language.replace(". eu .",". Basque .")
                language = language.replace(". be .",". Belarusian .")
                language = language.replace(". bn .",". Bengali .")
                language = language.replace(". bh .",". Bihari .")
                language = language.replace(". bi .",". Bislama .")
                language = language.replace(". bm .",". Bambara .")
                language = language.replace(". bs .",". Bosnian .")
                language = language.replace(". br .",". Breton .")
                language = language.replace(". bg .",". Bulgarian .")
                language = language.replace(". my .",". Burmese .")
                language = language.replace(". ca .",". Catalan .")
                language = language.replace(". ch .",". Chamorro .")
                language = language.replace(". ce .",". Chechen .")
                language = language.replace(". ny .",". Chewa .")
                language = language.replace(". zh .",". Chinese .")
                language = language.replace(". cv .",". Chuvash .")
                language = language.replace(". kw .",". Cornish .")
                language = language.replace(". co .",". Corsican .")
                language = language.replace(". cr .",". Cree .")
                language = language.replace(". hr .",". Croatian .")
                language = language.replace(". cs .",". Czech .")
                language = language.replace(". da .",". Danish .")
                language = language.replace(". dv .",". Maldivian .")
                language = language.replace(". nl .",". Dutch .")
                language = language.replace(". en .",". English .")
                language = language.replace(". eo .",". Esperanto .")
                language = language.replace(". et .",". Estonian .")
                language = language.replace(". ee .",". Ewe .")
                language = language.replace(". fo .",". Faroese .")
                language = language.replace(". fj .",". Fijian .")
                language = language.replace(". fi .",". Finnish .")
                language = language.replace(". fr .",". French .")
                language = language.replace(". ff .",". Pular .")
                language = language.replace(". gl .",". Galician .")
                language = language.replace(". ka .",". Georgian .")
                language = language.replace(". de .",". German .")
                language = language.replace(". el .",". Greek .")
                language = language.replace(". gn .",". Guaraní .")
                language = language.replace(". gu .",". Gujarati .")
                language = language.replace(". ht .",". Haitian .")
                language = language.replace(". ha .",". Hausa .")
                language = language.replace(". he .",". Hebrew .")
                language = language.replace(". hz .",". Herero .")
                language = language.replace(". hi .",". Hindi .")
                language = language.replace(". ho .",". Hiri Motu .")
                language = language.replace(". hu .",". Hungarian .")
                language = language.replace(". ia .",". Interlingua .")
                language = language.replace(". id .",". Indonesian .")
                language = language.replace(". ie .",". Interlingue .")
                language = language.replace(". ga .",". Irish .")
                language = language.replace(". ig .",". Igbo .")
                language = language.replace(". ik .",". Inupiaq .")
                language = language.replace(". io .",". Ido .")
                language = language.replace(". is .",". Icelandic .")
                language = language.replace(". it .",". Italian .")
                language = language.replace(". iu .",". Inuktitut .")
                language = language.replace(". ja .",". Japanese .")
                language = language.replace(". jv .",". Javanese .")
                language = language.replace(". kl .",". Greenlandic .")
                language = language.replace(". kn .",". Kannada .")
                language = language.replace(". kr .",". Kanuri .")
                language = language.replace(". ks .",". Kashmiri .")
                language = language.replace(". kk .",". Kazakh .")
                language = language.replace(". km .",". Khmer .")
                language = language.replace(". ki .",". Kikuyu .")
                language = language.replace(". rw .",". Kinyarwanda .")
                language = language.replace(". ky .",". Kirghiz .")
                language = language.replace(". kv .",". Komi .")
                language = language.replace(". kg .",". Kongo .")
                language = language.replace(". ko .",". Korean .")
                language = language.replace(". ku .",". Kurdish .")
                language = language.replace(". kj .",". Kwanyama .")
                language = language.replace(". la .",". Latin .")
                language = language.replace(". lb .",". Luxembourgish .")
                language = language.replace(". lg .",". Luganda .")
                language = language.replace(". li .",". Limburgish .")
                language = language.replace(". ln .",". Lingala .")
                language = language.replace(". lo .",". Lao .")
                language = language.replace(". lt .",". Lithuanian .")
                language = language.replace(". lu .",". Luba-Katanga .")
                language = language.replace(". lv .",". Latvian .")
                language = language.replace(". gv .",". Manx .")
                language = language.replace(". mk .",". Macedonian .")
                language = language.replace(". mg .",". Malagasy .")
                language = language.replace(". ms .",". Malay .")
                language = language.replace(". ml .",". Malayalam .")
                language = language.replace(". mt .",". Maltese .")
                language = language.replace(". mi .",". Māori .")
                language = language.replace(". mr .",". Marathi .")
                language = language.replace(". mh .",". Marshallese .")
                language = language.replace(". mn .",". Mongolian .")
                language = language.replace(". na .",". Nauru .")
                language = language.replace(". nv .",". Navajo .")
                language = language.replace(". nb .",". Norwegian Bokmål .")
                language = language.replace(". nd .",". North Ndebele .")
                language = language.replace(". ne .",". Nepali .")
                language = language.replace(". ng .",". Ndonga .")
                language = language.replace(". nn .",". Norwegian Nynorsk .")
                language = language.replace(". no .",". Norwegian .")
                language = language.replace(". ii .",". Nuosu .")
                language = language.replace(". nr .",". South Ndebele .")
                language = language.replace(". oc .",". Occitan .")
                language = language.replace(". oj .",". Ojibwe .")
                language = language.replace(". cu .",". Slavonic .")
                language = language.replace(". om .",". Oromo .")
                language = language.replace(". or .",". Oriya .")
                language = language.replace(". os .",". Ossetian .")
                language = language.replace(". pa .",". Panjabi .")
                language = language.replace(". pi .",". Pāli .")
                language = language.replace(". fa .",". Persian .")
                language = language.replace(". pl .",". Polish .")
                language = language.replace(". ps .",". Pashto .")
                language = language.replace(". pt .",". Portuguese .")
                language = language.replace(". qu .",". Quechua .")
                language = language.replace(". rm .",". Romansh .")
                language = language.replace(". rn .",". Kirundi .")
                language = language.replace(". ro .",". Romanian .")
                language = language.replace(". ru .",". Russian .")
                language = language.replace(". sa .",". Sanskrit .")
                language = language.replace(". sc .",". Sardinian .")
                language = language.replace(". sd .",". Sindhi .")
                language = language.replace(". se .",". Northern Sami .")
                language = language.replace(". sm .",". Samoan .")
                language = language.replace(". sg .",". Sango .")
                language = language.replace(". sr .",". Serbian .")
                language = language.replace(". gd .",". Gaelic .")
                language = language.replace(". sn .",". Shona .")
                language = language.replace(". si .",". Sinhala .")
                language = language.replace(". sk .",". Slovak .")
                language = language.replace(". sl .",". Slovene .")
                language = language.replace(". so .",". Somali .")
                language = language.replace(". st .",". Southern Sotho .")
                language = language.replace(". es .",". Spanish .")
                language = language.replace(". su .",". Sundanese .")
                language = language.replace(". sw .",". Swahili .")
                language = language.replace(". ss .",". Swati .")
                language = language.replace(". sv .",". Swedish .")
                language = language.replace(". ta .",". Tamil .")
                language = language.replace(". te .",". Telugu .")
                language = language.replace(". tg .",". Tajik .")
                language = language.replace(". th .",". Thai .")
                language = language.replace(". ti .",". Tigrinya .")
                language = language.replace(". bo .",". Tibetan .")
                language = language.replace(". tk .",". Turkmen .")
                language = language.replace(". tl .",". Tagalog .")
                language = language.replace(". tn .",". Tswana .")
                language = language.replace(". to .",". Tonga .")
                language = language.replace(". tr .",". Turkish .")
                language = language.replace(". ts .",". Tsonga .")
                language = language.replace(". tt .",". Tatar .")
                language = language.replace(". tw .",". Twi .")
                language = language.replace(". ty .",". Tahitian .")
                language = language.replace(". ug .",". Uighur .")
                language = language.replace(". uk .",". Ukrainian .")
                language = language.replace(". ur .",". Urdu .")
                language = language.replace(". uz .",". Uzbek .")
                language = language.replace(". ve .",". Venda .")
                language = language.replace(". vi .",". Vietnamese .")
                language = language.replace(". vo .",". Volapük .")
                language = language.replace(". wa .",". Walloon .")
                language = language.replace(". cy .",". Welsh .")
                language = language.replace(". wo .",". Wolof .")
                language = language.replace(". fy .",". Western Frisian .")
                language = language.replace(". xh .",". Xhosa .")
                language = language.replace(". yi .",". Yiddish .")
                language = language.replace(". yo .",". Yoruba .")
                language = language.replace(". za .",". Zhuang .")
                language = language.replace(". ","")
                language = language.replace(" .","")
                language = language.replace(" , ",", ")
                return language
            }
            false -> {
                return lang.replace(",", ", ")
            }
        }
    }
}