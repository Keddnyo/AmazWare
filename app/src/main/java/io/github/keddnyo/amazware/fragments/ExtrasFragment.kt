package io.github.keddnyo.amazware.fragments

import android.R.attr.label
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import androidx.core.content.ContextCompat.getSystemService
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

        okHttpClient.newCall(requestMain).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                val json = JSONObject(response.body()!!.string())

                val output = json.toString()
                output.replace(",", ", ")
                output.substringBefore('#')

                deviceSpinner.post{
                    devList.add(getString(R.string.manual_input))
                    devAdapter.notifyDataSetChanged()
                }
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
                                    list.add(
                                        Adapter(
                                            getString(R.string.lang),
                                            lang
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
                                    val changelog = json.getString("changeLog") // changeLog
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
}