package io.github.keddnyo.amazware.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.github.keddnyo.amazware.R
import io.github.keddnyo.amazware.utils.Adapter
import io.github.keddnyo.amazware.utils.Device
import io.github.keddnyo.amazware.utils.Lang
import io.github.keddnyo.amazware.utils.MakeRequest
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class Feed : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = getString(R.string.feed_title) // New title

        val refresh = findViewById<SwipeRefreshLayout>(R.id.feed_refresh)

        init() // First load

        refresh.setOnRefreshListener { // Pull refresh
            init()
        }
    }

    private fun init() {
        val okHttpClient = OkHttpClient()
        val deviceIndex: ListView = findViewById(R.id.feedView)
        val firmwareString = getString(R.string.firmware_fw)
        val languagesString = getString(R.string.firmware_lang)
        val changelogString = getString(R.string.firmware_change_log)
        val dateString = getString(R.string.firmware_date)
        val request = MakeRequest().getLatest()
        val refresh: SwipeRefreshLayout = findViewById(R.id.feed_refresh)
        var indexes = arrayOf<Int>()

        refresh.isRefreshing = true

        val list = ArrayList<Adapter>() // Setting adapter
        val adapter = SimpleAdapter(
            this,
            list,
            android.R.layout.simple_list_item_2,
            arrayOf(Adapter.NAME, Adapter.DESCRIPTION),
            intArrayOf(
                android.R.id.text1, android.R.id.text2
            )
        )
        deviceIndex.adapter = adapter

        okHttpClient.newCall(request).enqueue(object : Callback { // Creating request
            override fun onFailure(call: Call, e: IOException) { // Error
                runOnUiThread {
                    refresh.isRefreshing = false
                    Toast.makeText(this@Feed, getString(R.string.failed), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) { // Success
                val json = JSONObject(response.body()!!.string())
                val array = json.toMap()
                val keys = array.keys

                try {
                    for (i in keys) { // Device indexes
                        if (json.has(i)) { // Existing indexes
                            val deviceName = Device().name(i) // Device name
                            val firmware = json.getJSONObject(i).getString("fw")
                                .toString() // Firmware
                            val languages = json.getJSONObject(i).getString("languages")
                                .toString() // Languages
                            val languageNames = Lang().rename(this@Feed, languages)
                            var changelog = json.getJSONObject(i).getString("changelog")
                                .toString() // Changelog
                            changelog = changelog.substringBefore('#')
                            val date = json.getJSONObject(i).getString("date")
                                .toString() // Date

                            deviceIndex.post {
                                if (changelog.isEmpty()) { // An empty changelog won't be shown
                                    list.add(
                                        Adapter(
                                            deviceName,
                                            "$firmwareString: $firmware\n\n$languagesString: $languageNames\n\n$dateString: $date\n"
                                        )
                                    )
                                } else { // A non-empty changelog will be shown
                                    list.add(
                                        Adapter(
                                            deviceName,
                                            "$firmwareString: $firmware\n\n$languagesString: $languageNames\n\n$changelogString:\n$changelog\n\n$dateString: $date\n"
                                        )
                                    )
                                }
                                adapter.notifyDataSetChanged() // Commit changes

                                indexes = append(indexes, i.toInt())
                            }
                            refresh.post {
                                refresh.isRefreshing = false
                            }
                        }
                    }
                } catch (e: IOException) {
                    refresh.post {
                        refresh.isRefreshing = false
                        Toast.makeText(this@Feed, getString(R.string.failed), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })

        deviceIndex.onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
                openExtras(indexes[position])
            }
    }

    private fun append(arr: Array<Int>, element: Int): Array<Int> {
        val list: MutableList<Int> = arr.toMutableList()
        list.add(element)
        return list.toTypedArray()
    }

    private fun openExtras(index: Int) {
        val intent = Intent(this@Feed, ExtrasDialog::class.java)
        intent.putExtra("deviceSource", index)
        startActivity(intent)
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