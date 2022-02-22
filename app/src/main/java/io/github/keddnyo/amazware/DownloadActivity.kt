package io.github.keddnyo.amazware

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.SimpleAdapter
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class DownloadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)
    }

    override fun onResume() {
        super.onResume()
        val name = intent.getIntExtra("index",0)

        val okHttpClient = OkHttpClient()
        val urlMain = "https://schakal.ru/fw/latest.json"
        val requestMain = Request.Builder().url(urlMain).build()
        val downloadList = findViewById<ListView>(R.id.downloadList)

        val list = ArrayList<Adapter>()
        val adapter = SimpleAdapter(
            this, list, android.R.layout.simple_list_item_2, arrayOf(Adapter.NAME, Adapter.FIRMWARE), intArrayOf(
                android.R.id.text1, android.R.id.text2
            )
        )
        downloadList.adapter = adapter

        okHttpClient.newCall(requestMain).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val json = JSONObject(response.body()!!.string())

                try {
                    if (json.has(name.toString())) {
                        val deviceName = Device().name(name.toString())
                        val firmware = json.getJSONObject(name.toString()).getString("fw").toString()
                        val languages = json.getJSONObject(name.toString()).getString("languages").toString()
                        val changelog = json.getJSONObject(name.toString()).getString("changelog").toString()
                        runOnUiThread {
                            list.add(Adapter("Device name", deviceName))
                            list.add(Adapter("Firmware version", firmware))
                            list.add(Adapter("Languages", languages))
                            list.add(Adapter("Changelog", changelog))
                            adapter.notifyDataSetChanged()
                        }
                    }
                } catch (e: IOException) {
                }
            }
        })
    }
}