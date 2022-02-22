package io.github.keddnyo.amazware

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView
import okhttp3.*
import org.json.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        val url = "https://schakal.ru/fw/latest.json"
        val okHttpClient = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val deviceIndex = findViewById<ListView>(R.id.deviceIndex)

        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            android.R.id.text1
        )
        deviceIndex.adapter = adapter

        okHttpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val json = JSONObject(response.body()!!.string())

                try {
                    for (i in 1 .. 100) {
                        if (json.has(i.toString())) {
                            val jsonObject = json.getString(i.toString()).toString()

                            val deviceName = if (Device().name(i.toString()) != "Unknown") {
                                Device().name(i.toString())
                            } else {
                                jsonObject
                            }

                            //val deviceName = Device().name(i.toString())
                            runOnUiThread {
                                adapter.add(deviceName)
                            }
                        }
                    }
                } catch (e: IOException) {
                }
            }
        })
    }
}