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

        title = getString(R.string.extras_title) // New Title
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
        val buttonImport: Button = findViewById(R.id.buttonImport)
        val buttonSubmit: Button = findViewById(R.id.buttonSubmit)
        val error = getString(R.string.firmware_empty_field)

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

        devList.add(getString(R.string.extras_manual))
        devAdapter.notifyDataSetChanged()
        appName.adapter = appSpinner

        Toast.makeText(context, getString(R.string.extras_gathering_info), Toast.LENGTH_SHORT).show()

        if ((sharedPreferences.getString(
                "productionSource",
                ""
            ) != "") || (sharedPreferences.getString(
                "deviceSource",
                ""
            ) != "") || (sharedPreferences.getString(
                "appVersion",
                ""
            ) != "")
        ) {
            buttonImport.visibility = View.VISIBLE
        } else {
            buttonImport.visibility = View.GONE
        }

        okHttpClient.newCall(deviceList).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(context, getString(R.string.failed), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = JSONObject(response.body()!!.string())
                val array = json.toMap()
                val keys = array.keys

                for (i in keys) {
                    val deviceIntent = getIntent()
                    val deviceSourceIntent = deviceIntent.getIntExtra("deviceSource", 0)

                    if (json.has(i)) {
                        deviceSpinner.post {
                            val name = json.getJSONObject(i)
                                .getString("name") // Filling dropdown list
                            devList.add(name)
                            devAdapter.notifyDataSetChanged()

                            // Load device values after opening Activity from Feed
                            if (deviceSourceIntent != 0) {
                                if (json.has(deviceSourceIntent.toString())) {
                                    val spinnerPosition: Int = devAdapter.getPosition(json.getJSONObject(
                                        deviceSourceIntent.toString()
                                    ).getString("name"))
                                    deviceSpinner.setSelection(spinnerPosition)
                                } else {
                                    finish()
                                }
                            }
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

                        productionSource.error = null
                        deviceSource.error = null
                        appVersion.error = null

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
                                } else if (selectedItem == getString(R.string.extras_manual)) {
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
            when {
                productionSource.text.isEmpty() -> {
                    productionSource.error = error
                }
                deviceSource.text.isEmpty() -> {
                    deviceSource.error = error
                }
                appVersion.text.isEmpty() -> {
                    appVersion.error = error
                }
                else -> {
                    // Init serverRequest val here because we're communicate with the EditText
                    MakeRequest().firmwareRequest(
                        context,
                        productionSource.text.toString(),
                        deviceSource.text.toString(),
                        appVersion.text.toString(),
                        appName.selectedItem.toString(),
                    )
                }
            }
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