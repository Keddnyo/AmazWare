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
        init()
    }

    private fun init() {
        requireActivity().title = getString(R.string.feed) // New title

        // Variables
        val okHttpClient = OkHttpClient()
        val urlMain = "https://schakal.ru/fw/latest.json"
        val requestMain = Request.Builder().url(urlMain).build()
        val deviceIndex = requireActivity().findViewById<ListView>(R.id.feedView)

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
        deviceIndex.adapter = adapter

        // Creating request
        okHttpClient.newCall(requestMain).enqueue(object : Callback {
            // Error
            override fun onFailure(call: Call, e: IOException) {
                requireActivity().title = getString(R.string.error)
            }

            // Success
            override fun onResponse(call: Call, response: Response) {
                val json = JSONObject(response.body()!!.string())

                try {
                    for (i in 1..1000) { // Device indexes
                        if (json.has(i.toString())) { // Existing indexes
                            // Resource strings
                            val firmwareString = getString(R.string.firmwareVersion)
                            val languagesString = getString(R.string.lang)
                            val changelogString = getString(R.string.changeLog)
                            val dateString = getString(R.string.date)

                            // Values
                            val deviceName =
                                Device().name(i.toString()) // Device name
                            val firmware =
                                json.getJSONObject(i.toString()).getString("fw").toString() // Firmware
                            val languages =
                                json.getJSONObject(i.toString()).getString("languages").toString() // Languages
                            val languageNames = ExtrasFragment().replace(languages)
                            var changelog =
                                json.getJSONObject(i.toString()).getString("changelog").toString() // Changelog
                            changelog = changelog.substringBefore('#')
                            val date =
                                json.getJSONObject(i.toString()).getString("date").toString() // Date

                            deviceIndex.post {
                                if (changelog == "") { // An empty changelog won't be shown
                                    list.add(
                                        Adapter(
                                            deviceName,
                                            "$firmwareString: $firmware\n$languagesString: $languageNames\n\n$dateString: $date\n"
                                        )
                                    )
                                } else { // A non-empty changelog will be shown
                                    list.add(
                                        Adapter(
                                            deviceName,
                                            "$firmwareString: $firmware\n$languagesString: $languageNames\n\n$changelogString:\n$changelog\n\n$dateString: $date\n"
                                        )
                                    )
                                }
                                adapter.notifyDataSetChanged() // Commit changes
                            }
                        }
                    }
                } catch (e: IOException) {
                    activity!!.title = getString(R.string.error)
                }
            }
        })

        // Pull refresh
        val feedRefresh =
            requireActivity().findViewById<androidx.swiperefreshlayout.widget.SwipeRefreshLayout>(R.id.feed_refresh)
        feedRefresh.setOnRefreshListener {
            list.clear()
            adapter.notifyDataSetChanged()
            init()
            feedRefresh.isRefreshing = false
        }
    }
}