package io.github.keddnyo.amazware

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import android.widget.SimpleAdapter
import okhttp3.*
import org.json.JSONObject
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

        val list = ArrayList<Adapter>()
        val adapter = SimpleAdapter(
            this, list, android.R.layout.simple_list_item_2, arrayOf(Adapter.NAME, Adapter.FIRMWARE), intArrayOf(
                android.R.id.text1, android.R.id.text2
            )
        )
        deviceIndex.adapter = adapter

        okHttpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val json = JSONObject(response.body()!!.string())

                try {
                    for (i in 1 .. 300) {
                        if (json.has(i.toString())) {
                            val deviceName = Device().name(i.toString())
                            runOnUiThread {
                                list.add(Adapter(deviceName, "firmware"))
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                } catch (e: IOException) {
                }
            }
        })
    }
}