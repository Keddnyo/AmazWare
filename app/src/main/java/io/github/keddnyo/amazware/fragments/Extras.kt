package io.github.keddnyo.amazware.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import io.github.keddnyo.amazware.R
import io.github.keddnyo.amazware.fragments.utils.Adapter
import io.github.keddnyo.amazware.fragments.utils.Download
import io.github.keddnyo.amazware.fragments.utils.Lang
import io.github.keddnyo.amazware.fragments.utils.MakeRequest
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class Extras : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_extras, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = getString(R.string.extras) // New Title
        val okHttpClient = OkHttpClient()
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(requireActivity())
        val deviceSpinner = requireActivity().findViewById<Spinner>(R.id.deviceList)
        val productionSource: EditText = requireActivity().findViewById(R.id.productionSource)
        val deviceSource: EditText = requireActivity().findViewById(R.id.deviceSource)
        val appVersion: EditText = requireActivity().findViewById(R.id.appVersion)
        val appName: EditText = requireActivity().findViewById(R.id.appName)
        val buttonReset = requireActivity().findViewById<Button>(R.id.buttonReset)
        val buttonSubmit = requireActivity().findViewById<Button>(R.id.buttonSubmit)
        val responseList = requireActivity().findViewById<ListView>(R.id.responseList)
        val responseField = requireActivity().findViewById<TextView>(R.id.responseField)
        val none = getString(R.string.none)

        val devList = ArrayList<String>()
        val devAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, devList)
        val deviceList = MakeRequest().getDevices()

        devList.add(getString(R.string.manual_input))
        devAdapter.notifyDataSetChanged()

        okHttpClient.newCall(deviceList).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val json = JSONObject(response.body()!!.string())
                for (i in 1..1000) {
                    if (json.has(i.toString())) {
                        deviceSpinner.post {
                            val name = json.getJSONObject(i.toString())
                                .getString("name") // Filling dropdown list
                            devList.add(name)
                            devAdapter.notifyDataSetChanged()
                        }
                    }
                }

                deviceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        pos: Int,
                        id: Long
                    ) {
                        val selectedItem = deviceSpinner.selectedItem.toString()

                        for (i in 1..1000) {
                            if (json.has(i.toString())) { // Existing indexes
                                val nameResult = json.getJSONObject(i.toString()).getString("name")
                                val productionSourceResult =
                                    json.getJSONObject(i.toString()).getString("productionSource")
                                val appnameResult =
                                    json.getJSONObject(i.toString()).getString("appname")
                                val appVersionResult =
                                    json.getJSONObject(i.toString()).getString("appVersion")

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
                                }
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        })
        deviceSpinner.adapter = devAdapter

        val list = ArrayList<Adapter>() // Setting response adapter
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

        buttonReset.setOnClickListener {
            responseList.visibility = View.GONE
            responseField.text = null
            list.clear()
            deviceSpinner.setSelection(0)
            productionSource.text.clear()
            deviceSource.text.clear()
            appVersion.text.clear()
            appName.text.clear()
        }

        buttonSubmit.setOnLongClickListener {
            list.clear()
            responseList.visibility = View.GONE

            // Init serverRequest val here because we're communicate with EditText
            val serverRequest =
                MakeRequest().directDevice(productionSource.text.toString(), deviceSource.text.toString(), appVersion.text.toString(), appName.text.toString())

            okHttpClient.newCall(serverRequest).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    requireActivity().title = getString(R.string.error)
                }

                override fun onResponse(call: Call, response: Response) {
                    responseField.post {
                        responseField.text = serverRequest.toString()
                    }
                }
            })
            true
        }

        buttonSubmit.setOnClickListener {
            list.clear() // Clear for a new list
            responseField.text = null

            // Init serverRequest val here because we're communicate with EditText
            val serverRequest =
                MakeRequest().directDevice(productionSource.text.toString(), deviceSource.text.toString(), appVersion.text.toString(), appName.text.toString())

            okHttpClient.newCall(serverRequest).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    requireActivity().title = getString(R.string.error)
                }

                override fun onResponse(call: Call, response: Response) {
                    val json = JSONObject(response.body()!!.string())

                    when (sharedPreferences.getBoolean("simple_response", true)) {
                        false -> {
                            responseField.post {
                                responseField.text = json.toString()
                            }
                        }
                        true -> {
                            responseList.post {
                                responseList.visibility = View.VISIBLE

                                if (json.has("firmwareVersion")) {
                                    val firmwareVersion =
                                        json.getString("firmwareVersion") // Firmware
                                    val firmwareMd5 = json.getString("firmwareMd5") // Firmware MD5
                                    list.add(
                                        Adapter(
                                            getString(R.string.firmware_version) + ": " + firmwareVersion,
                                            "MD5: $firmwareMd5"
                                        )
                                    )
                                    adapter.notifyDataSetChanged() // Commit changes
                                } else {
                                    list.add(
                                        Adapter(
                                            getString(R.string.firmware_version) + ": " + none,
                                            "MD5: $none"
                                        )
                                    )
                                    adapter.notifyDataSetChanged() // Commit changes
                                }
                                if (json.has("resourceVersion")) {
                                    val resourceVersion =
                                        json.getString("resourceVersion") // Resources
                                    val resourceMd5 = json.getString("resourceMd5") // Resources MD5
                                    list.add(
                                        Adapter(
                                            getString(R.string.resource_version) + ": " + resourceVersion,
                                            "MD5: $resourceMd5"
                                        )
                                    )
                                    adapter.notifyDataSetChanged() // Commit changes
                                } else {
                                    list.add(
                                        Adapter(
                                            getString(R.string.resource_version) + ": " + none,
                                            "MD5: $none"
                                        )
                                    )
                                    adapter.notifyDataSetChanged() // Commit changes
                                }
                                if (json.has("baseResourceVersion")) {
                                    val baseResourceVersion =
                                        json.getString("baseResourceVersion") // Base resources
                                    val baseResourceMd5 =
                                        json.getString("baseResourceMd5") // Base resources MD5
                                    list.add(
                                        Adapter(
                                            getString(R.string.base_resource_version) + ": " + baseResourceVersion,
                                            "MD5: $baseResourceMd5"
                                        )
                                    )
                                    adapter.notifyDataSetChanged() // Commit changes
                                } else {
                                    list.add(
                                        Adapter(
                                            getString(R.string.base_resource_version) + ": " + none,
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
                                            getString(R.string.font_version) + ": " + fontVersion,
                                            "MD5: $fontMd5"
                                        )
                                    )
                                    adapter.notifyDataSetChanged() // Commit changes
                                } else {
                                    list.add(
                                        Adapter(
                                            getString(R.string.font_version) + ": " + none,
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
                                            getString(R.string.gps_version) + ": " + gpsVersion,
                                            "MD5: $gpsMd5"
                                        )
                                    )
                                    adapter.notifyDataSetChanged() // Commit changes
                                } else {
                                    list.add(
                                        Adapter(
                                            getString(R.string.gps_version) + ": " + none,
                                            "MD5: $none"
                                        )
                                    )
                                    adapter.notifyDataSetChanged() // Commit changes
                                }
                                if (json.has("lang")) {
                                    val lang = json.getString("lang") // Languages
                                    val language = Lang().rename(requireActivity(), lang)
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
                                            getString(R.string.change_log),
                                            changelog
                                        )
                                    )
                                    adapter.notifyDataSetChanged() // Commit changes
                                }
                                if (!json.has("firmwareVersion")) {
                                    responseField.post {
                                        responseField.text = json.toString()
                                    }
                                    Toast.makeText(
                                        context,
                                        getString(R.string.firmware_not_found),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    requireActivity().title = getString(R.string.error)
                                }
                            }

                            responseList.onItemClickListener =
                                OnItemClickListener { _, _, position, _ ->
                                    when (position) {
                                        0 -> {
                                            if (json.has("firmwareUrl")) {
                                                val fileUrl = json.getString("firmwareUrl")
                                                context?.let {
                                                    Download().run(
                                                        it,
                                                        fileUrl,
                                                        "firmware",
                                                        "?"
                                                    )
                                                }
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    R.string.none,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                        1 -> {
                                            if (json.has("resourceUrl")) {
                                                val fileUrl = json.getString("resourceUrl")
                                                context?.let {
                                                    Download().run(
                                                        it,
                                                        fileUrl,
                                                        "resource",
                                                        "?"
                                                    )
                                                }
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    R.string.none,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                        2 -> {
                                            if (json.has("baseResourceUrl")) {
                                                val fileUrl = json.getString("baseResourceUrl")
                                                context?.let {
                                                    Download().run(
                                                        it,
                                                        fileUrl,
                                                        "base_resource",
                                                        "?"
                                                    )
                                                }
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    R.string.none,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                        3 -> {
                                            if (json.has("fontUrl")) {
                                                val fileUrl = json.getString("fontUrl")
                                                context?.let {
                                                    Download().run(
                                                        it,
                                                        fileUrl,
                                                        "font",
                                                        "?"
                                                    )
                                                }
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    R.string.none,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                        4 -> {
                                            if (json.has("gpsUrl")) {
                                                val fileUrl = json.getString("gpsUrl")
                                                context?.let {
                                                    Download().run(
                                                        it,
                                                        fileUrl,
                                                        "gps",
                                                        "?"
                                                    )
                                                }
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    R.string.none,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                        5 -> {
                                            if (json.has("lang")) {
                                                val lang = json.getString("lang")
                                                Toast.makeText(context, lang, Toast.LENGTH_SHORT)
                                                    .show()
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    R.string.none,
                                                    Toast.LENGTH_SHORT
                                                ).show()
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