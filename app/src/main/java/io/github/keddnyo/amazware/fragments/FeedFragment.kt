package io.github.keddnyo.amazware.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.fragment.app.Fragment
import io.github.keddnyo.amazware.Adapter
import io.github.keddnyo.amazware.Device
import io.github.keddnyo.amazware.R
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = ArrayList<Adapter>()
        val adapter = SimpleAdapter(
            activity,
            list,
            android.R.layout.simple_list_item_2,
            arrayOf(Adapter.NAME, Adapter.FIRMWARE),
            intArrayOf(
                android.R.id.text1, android.R.id.text2
            )
        )

        init()

        val feedRefresh =
            activity!!.findViewById<androidx.swiperefreshlayout.widget.SwipeRefreshLayout>(R.id.feed_refresh)
        feedRefresh.setOnRefreshListener {
            list.clear()
            adapter.notifyDataSetChanged()
            init()
            feedRefresh.isRefreshing = false
        }
    }

    private fun init() {
        activity!!.title = getString(R.string.feed)

        val okHttpClient = OkHttpClient()
        val urlMain = "https://schakal.ru/fw/latest.json"
        val requestMain = Request.Builder().url(urlMain).build()
        val deviceIndex = activity!!.findViewById<ListView>(R.id.feedView)

        val list = ArrayList<Adapter>()
        val adapter = SimpleAdapter(
            activity,
            list,
            android.R.layout.simple_list_item_2,
            arrayOf(Adapter.NAME, Adapter.FIRMWARE),
            intArrayOf(
                android.R.id.text1, android.R.id.text2
            )
        )

        deviceIndex.adapter = adapter

        okHttpClient.newCall(requestMain).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity!!.title = getString(R.string.error)
            }

            override fun onResponse(call: Call, response: Response) {
                val json = JSONObject(response.body()!!.string())

                try {
                    for (i in 1..1000) {
                        if (json.has(i.toString())) {
                            val deviceName = Device().name(i.toString())
                            val firmware =
                                json.getJSONObject(i.toString()).getString("fw").toString()
                            val languages =
                                json.getJSONObject(i.toString()).getString("languages").toString()
                            val changelog =
                                json.getJSONObject(i.toString()).getString("changelog").toString()
                            val date = json.getJSONObject(i.toString()).getString("date").toString()

                            deviceIndex.post {
                                if (changelog == "") {
                                    list.add(
                                        Adapter(
                                            deviceName,
                                            "Firmware: $firmware\nLanguages: $languages\n\nDate: $date\n"
                                        )
                                    )
                                } else {
                                    list.add(
                                        Adapter(
                                            deviceName,
                                            "Firmware: $firmware\nLanguages: $languages\n\nChangelog:\n$changelog\n\nDate: $date\n"
                                        )
                                    )
                                }
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