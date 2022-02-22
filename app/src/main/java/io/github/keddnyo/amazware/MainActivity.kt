package io.github.keddnyo.amazware

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView.*
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
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

        val okHttpClient = OkHttpClient()
        val urlMain = "https://schakal.ru/fw/latest.json"
        val requestMain = Request.Builder().url(urlMain).build()
        val deviceIndex = findViewById<ListView>(R.id.deviceList)

        val list = ArrayList<Adapter>()
        val adapter = SimpleAdapter(
            this, list, android.R.layout.simple_list_item_2, arrayOf(Adapter.NAME, Adapter.FIRMWARE), intArrayOf(
                android.R.id.text1, android.R.id.text2
            )
        )
        deviceIndex.adapter = adapter
        val intent = Intent(this, DownloadActivity::class.java)

        okHttpClient.newCall(requestMain).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val json = JSONObject(response.body()!!.string())

                try {
                    for (i in 1 .. 400) {
                        if (json.has(i.toString())) {
                            val deviceName = Device().name(i.toString())
                            val firmware = json.getJSONObject(i.toString()).getString("fw").toString()
                            val languages = json.getJSONObject(i.toString()).getString("languages").toString()
                            val changelog = json.getJSONObject(i.toString()).getString("changelog").toString()
                            runOnUiThread {
                                list.add(Adapter(deviceName, "Firmware: $firmware\nLanguages: $languages\nChangelog: $changelog" +
                                        "Languages: $languages"))
                                adapter.notifyDataSetChanged()
                            }
                            intent.putExtra("index", i.toString())
                        }
                    }
                } catch (e: IOException) {
                }
            }
        })

        deviceIndex.onItemClickListener = OnItemClickListener {
                parent, _, position, id ->
            val selectedItemText = parent.getItemAtPosition(position)
            Toast.makeText(this, id.toString(), Toast.LENGTH_SHORT).show()
            //intent.putExtra("index", list[position]["ID"].toString())
            //startActivity(intent)
        }
    }
}