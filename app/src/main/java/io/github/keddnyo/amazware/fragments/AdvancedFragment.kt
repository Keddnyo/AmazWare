package io.github.keddnyo.amazware.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import io.github.keddnyo.amazware.Adapter
import io.github.keddnyo.amazware.R
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class AdvancedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_advanced, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = getString(R.string.advanced) // New Title

        // Variables
        val deviceSource =  requireActivity().findViewById<EditText>(R.id.deviceSource)
        val productionSource =  requireActivity().findViewById<EditText>(R.id.productionSource)
        val appName =  requireActivity().findViewById<EditText>(R.id.appName)
        val appVersion =  requireActivity().findViewById<EditText>(R.id.appVersion)
        val appVersionBuild =  requireActivity().findViewById<EditText>(R.id.appVersionBuild)
        val appRadioGroup =  requireActivity().findViewById<RadioGroup>(R.id.appRadioGroup)
        val channelPlay =  requireActivity().findViewById<CheckBox>(R.id.channelPlay)
        val buttonReset =  requireActivity().findViewById<Button>(R.id.buttonReset)
        val buttonSubmit =  requireActivity().findViewById<Button>(R.id.buttonSubmit)

        appRadioGroup.setOnCheckedChangeListener { _, checkedId -> // find which radio button is selected
            if (checkedId == R.id.radioZepp) {
                appName.setText("com.huami.midong")
                channelPlay.isEnabled = true
            } else if (checkedId == R.id.radioMiFit) {
                channelPlay.isEnabled = false
                appName.setText("com.xiaomi.hm.health")
            }
        }

        val playPostfix = when (channelPlay.isChecked) {
            true -> {
                "-play_"
            }
            false -> {
                "_"
            }
        }

        appVersionBuild.addTextChangedListener (object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                buttonSubmit.isEnabled = deviceSource.text.isNotEmpty() && productionSource.text.isNotEmpty() && appName.text.isNotEmpty() && appVersion.text.isNotEmpty() && appVersionBuild.text.isNotEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        val responseList = requireActivity().findViewById<ListView>(R.id.responseList)

        // Setting adapter
        val list = ArrayList<Adapter>()
        val adapter = SimpleAdapter(
            activity,
            list,
            android.R.layout.simple_list_item_2,
            arrayOf(Adapter.NAME, Adapter.DESCRIPTION),
            intArrayOf(
                android.R.id.text1, android.R.id.text2
            )
        )
        responseList.adapter = adapter


        buttonReset.setOnClickListener {
            responseList.visibility = View.GONE
        }

        buttonSubmit.setOnClickListener {

            val okHttpClient = OkHttpClient()
            val requestHost = "api-mifit-ru.huami.com"

            val uriBuilder: Uri.Builder = Uri.Builder()
            uriBuilder.scheme("https")
                .authority(requestHost)
                .appendPath("devices")
                .appendPath("ALL")
                .appendPath("hasNewVersion")

                /**.appendQueryParameter("productId", "12")
                .appendQueryParameter("vendorSource", "1")
                .appendQueryParameter("resourceVersion", "0")
                .appendQueryParameter("firmwareFlag", "0")
                .appendQueryParameter("vendorId", "0")
                .appendQueryParameter("resourceFlag", "0")
                .appendQueryParameter("productionSource", "256")
                .appendQueryParameter("userid", "0")
                .appendQueryParameter("userId", "0")
                .appendQueryParameter("deviceSource", "")
                .appendQueryParameter("fontVersion", "0")
                .appendQueryParameter("fontFlag", "0")
                .appendQueryParameter("appVersion", "6.1.4-play_100440")
                .appendQueryParameter("appid", "0")
                .appendQueryParameter("callid", "0")
                .appendQueryParameter("channel", "0")
                .appendQueryParameter("country", "0")
                .appendQueryParameter("cv", "0")
                .appendQueryParameter("device", "")
                .appendQueryParameter("deviceType", "ALL")
                .appendQueryParameter("device_type", "0")
                .appendQueryParameter("firmwareVersion", "0")
                .appendQueryParameter("hardwareVersion", "0")
                .appendQueryParameter("lang", "0")
                .appendQueryParameter("support8Bytes", "true")
                .appendQueryParameter("timezone", "0")
                .appendQueryParameter("v", "0")
                .appendQueryParameter("gpsVersion", "0")
                .appendQueryParameter("baseResourceVersion", "0")**/

                .appendQueryParameter("productId", "0")
                .appendQueryParameter("vendorSource", "1")
                .appendQueryParameter("resourceVersion", "0")
                .appendQueryParameter("firmwareFlag", "1")
                .appendQueryParameter("vendorId", "343")
                .appendQueryParameter("resourceFlag", "7")
                .appendQueryParameter("productionSource", productionSource.text.toString())
                .appendQueryParameter("userid", "8719393185")
                .appendQueryParameter("userId", "8719393185")
                .appendQueryParameter("deviceSource", deviceSource.text.toString())
                .appendQueryParameter("fontVersion", "0")
                .appendQueryParameter("fontFlag", "3")
                .appendQueryParameter("appVersion", appVersion.text.toString() + playPostfix + appVersionBuild.text.toString())
                .appendQueryParameter("appid", "2882303761517383915")
                .appendQueryParameter("callid", "1614431188364")
                .appendQueryParameter("channel", "play")
                .appendQueryParameter("country", "CH")
                .appendQueryParameter("cv", "100395_5.12.2-play")
                .appendQueryParameter("device", "android_29")
                .appendQueryParameter("deviceType", "ALL")
                .appendQueryParameter("device_type", "android_phone")
                .appendQueryParameter("firmwareVersion", "0")
                .appendQueryParameter("hardwareVersion", "0.62.130.16")
                .appendQueryParameter("lang", "zh_CH")
                .appendQueryParameter("support8Bytes", "true")
                .appendQueryParameter("timezone", "Europe/Moscow")
                .appendQueryParameter("v", "2.0")
                .appendQueryParameter("gpsVersion", "0")
                .appendQueryParameter("baseResourceVersion", "0")

            val request = Request.Builder()
                .url(uriBuilder.toString())

                .addHeader("hm-privacy-diagnostics", "false")
                .addHeader("country", "AR")
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
                .addHeader("lang", "ar_AR")
                .addHeader("Host", "api-mifit-ru.huami.com")
                .addHeader("Connection", "Keep-Alive")
                .addHeader("accept-encoding", "gzip")
                .addHeader("accept", "*/*")

                .build()

            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    val json = JSONObject(response.body()!!.string())

                    responseList.post {
                        responseList.visibility = View.VISIBLE

                        if (json.has("firmwareVersion")) {
                            val firmwareVersion = json.getString("firmwareVersion") // Firmware
                            val firmwareUrl = json.getString("firmwareUrl") // Firmware Url
                            val firmwareMd5 = json.getString("firmwareMd5") // Firmware MD5
                            list.add(
                                Adapter(
                                    getString(R.string.firmwareVersion),
                                    firmwareVersion
                                )
                            )
                            list.add(
                                Adapter(
                                    getString(R.string.firmwareUrl),
                                    firmwareUrl
                                )
                            )
                            list.add(
                                Adapter(
                                    getString(R.string.firmwareMd5),
                                    firmwareMd5
                                )
                            )
                            adapter.notifyDataSetChanged() // Commit changes
                        }
                        if (json.has("resourceVersion")) {
                            val resourceVersion = json.getString("firmwareVersion") // Resources
                            val resourceUrl = json.getString("resourceUrl") // Resources Url
                            val resourceMd5 = json.getString("resourceMd5") // Resources MD5
                            list.add(
                                Adapter(
                                    getString(R.string.resourceVersion),
                                    resourceVersion
                                )
                            )
                            list.add(
                                Adapter(
                                    getString(R.string.resourceUrl),
                                    resourceUrl
                                )
                            )
                            list.add(
                                Adapter(
                                    getString(R.string.resourceMd5),
                                    resourceMd5
                                )
                            )
                            adapter.notifyDataSetChanged() // Commit changes
                        }
                        if (json.has("fontVersion")) {
                            val fontVersion = json.getString("firmwareVersion") // Font
                            val fontUrl = json.getString("firmwareUrl") // Font Url
                            val fontMd5 = json.getString("firmwareMd5") // Font MD5
                            list.add(
                                Adapter(
                                    getString(R.string.fontVersion),
                                    fontVersion
                                )
                            )
                            list.add(
                                Adapter(
                                    getString(R.string.fontUrl),
                                    fontUrl
                                )
                            )
                            list.add(
                                Adapter(
                                    getString(R.string.fontMd5),
                                    fontMd5
                                )
                            )
                            adapter.notifyDataSetChanged() // Commit changes
                        }
                        if (json.has("changelog")) {
                            val changelog = json.getString("chanelog") // Chanelog
                            list.add(
                                Adapter(
                                    getString(R.string.changelog),
                                    changelog
                                )
                            )
                            adapter.notifyDataSetChanged() // Commit changes
                        }
                    }
                }
            })
        }
    }
}