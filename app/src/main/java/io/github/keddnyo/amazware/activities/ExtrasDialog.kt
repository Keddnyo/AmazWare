package io.github.keddnyo.amazware.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import io.github.keddnyo.amazware.R
import io.github.keddnyo.amazware.utils.MakeRequest
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class ExtrasDialog : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.setTheme(R.style.dialog)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.extras_dialog)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = getString(R.string.extras) // New Title
    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPreferences.edit()

        val okHttpClient = OkHttpClient()
        val deviceSpinner: Spinner = findViewById(R.id.deviceList)
        val productionSource: EditText = findViewById(R.id.productionSource)
        val deviceSource: EditText = findViewById(R.id.deviceSource)
        val appVersion: EditText = findViewById(R.id.appVersion)
        val appName: Spinner = findViewById(R.id.appName)
        val buttonImport = findViewById<Button>(R.id.buttonImport)
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)

        val devList = ArrayList<String>()
        val devAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, devList)
        val deviceList = MakeRequest().getDevices()
        val context = this@ExtrasDialog
        val intent = Intent(context, ExtrasResponse::class.java)

        val appname = ArrayList<String>()
        val appSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, appname)
        val zepp = "com.huami.midong"
        val mifit = "com.xiaomi.hm.health"
        appname.add(zepp) // 0
        appname.add(mifit) // 1
        appSpinner.notifyDataSetChanged()
        appName.setSelection(0)

        devList.add(getString(R.string.manual_input))
        devAdapter.notifyDataSetChanged()
        appName.adapter = appSpinner

        if ((sharedPreferences.getString(
                "productionSource",
                ""
            ) != "") || (sharedPreferences.getString(
                "deviceSource",
                ""
            ) != "") || (sharedPreferences.getString(
                "appVersion",
                ""
            ) != "") || (sharedPreferences.getString("appname", "") != "")
        ) {
            buttonImport.visibility = View.VISIBLE
        } else {
            buttonImport.visibility = View.GONE
        }

        okHttpClient.newCall(deviceList).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val json = JSONObject(response.body()!!.string())
                val array = json.toMap()
                val keys = array.keys

                for (i in keys) {
                    if (json.has(i)) {
                        deviceSpinner.post {
                            val name = json.getJSONObject(i)
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
                                    intent.putExtra("title", nameResult)

                                    deviceSource.setText(i.toString())
                                    productionSource.setText(productionSourceResult)
                                    val index = when (appnameResult) {
                                        zepp -> {
                                            0 // zepp
                                        }
                                        mifit -> {
                                            1 // mifit
                                        }
                                        else -> {
                                            0 // zepp
                                        }
                                    }
                                    appName.setSelection(index)
                                    appVersion.setText(appVersionResult)

                                    deviceSource.isEnabled = false
                                    productionSource.isEnabled = false
                                } else if (selectedItem == getString(R.string.manual_input)) {
                                    intent.putExtra("title", getString(R.string.server_response))

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

        buttonSubmit.setOnClickListener {
            title = getString(R.string.extras)

            // Init serverRequest val here because we're communicate with EditText
            val serverRequest =
                MakeRequest().directDevice(
                    productionSource.text.toString(),
                    deviceSource.text.toString(),
                    appVersion.text.toString(),
                    appName.selectedItem.toString()
                )

            okHttpClient.newCall(serverRequest).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    title = getString(R.string.error)
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
                    if (!json.has("firmwareVersion")) {
                        runOnUiThread {
                            Toast.makeText(
                                context,
                                getString(R.string.firmware_not_found),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        finish()
                        startActivity(intent)
                    }
                }
            })
        }

        buttonImport.setOnClickListener {
            deviceSpinner.setSelection(0)
            productionSource.setText(sharedPreferences.getString("productionSource", ""))
            deviceSource.setText(sharedPreferences.getString("deviceSource", ""))
            appVersion.setText(sharedPreferences.getString("appVersion", ""))
            appName.setSelection(sharedPreferences.getInt("appname", 0))
            intent.putExtra("title", getString(R.string.server_response))
        }

        buttonImport.setOnLongClickListener {
            editor.remove("productionSource")
            editor.remove("deviceSource")
            editor.remove("appVersion")
            editor.remove("appname")
            editor.apply()
            buttonImport.visibility = View.GONE
            true
        }

        buttonSubmit.setOnLongClickListener {
            editor.putString("productionSource", productionSource.text.toString())
            editor.putString("deviceSource", deviceSource.text.toString())
            editor.putString("appVersion", appVersion.text.toString())
            editor.putInt("appname", appName.selectedItemId.toInt())
            editor.apply()
            buttonImport.visibility = View.VISIBLE
            true
        }
    }

    private fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith { it ->
        when (val value = this[it]) {
            is JSONArray -> {
                val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
                JSONObject(map).toMap().values.toList()
            }
            is JSONObject -> value.toMap()
            JSONObject.NULL -> null
            else -> value
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}