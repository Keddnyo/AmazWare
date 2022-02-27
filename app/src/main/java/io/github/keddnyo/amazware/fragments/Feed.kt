package io.github.keddnyo.amazware.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.github.keddnyo.amazware.fragments.utils.Adapter
import io.github.keddnyo.amazware.fragments.utils.Device
import io.github.keddnyo.amazware.R
import io.github.keddnyo.amazware.fragments.utils.Lang
import io.github.keddnyo.amazware.fragments.utils.MakeRequest
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class Feed : Fragment() {

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

        val okHttpClient = OkHttpClient()
        val deviceIndex = requireActivity().findViewById<ListView>(R.id.feedView)
        val feedRefresh = requireActivity().findViewById<SwipeRefreshLayout>(R.id.feed_refresh)
        val firmwareString = getString(R.string.firmware_version)
        val languagesString = getString(R.string.lang)
        val changelogString = getString(R.string.change_log)
        val dateString = getString(R.string.date)
        val request = MakeRequest().getLatest()

        val list = ArrayList<Adapter>() // Setting adapter
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

        okHttpClient.newCall(request).enqueue(object : Callback { // Creating request
            override fun onFailure(call: Call, e: IOException) { // Error
                requireActivity().title = getString(R.string.error)
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
                            val languageNames = activity?.let { Lang().rename(it, languages) }
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
                            }
                        }
                    }
                } catch (e: IOException) {
                    activity!!.title = getString(R.string.error)
                }
            }
        })

        feedRefresh.setOnRefreshListener { // Pull refresh
            list.clear()
            adapter.notifyDataSetChanged()
            init()
            feedRefresh.isRefreshing = false
        }
    }

    private fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith { it ->
        when (val value = this[it])
        {
            is JSONArray ->
            {
                val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
                JSONObject(map).toMap().values.toList()
            }
            is JSONObject -> value.toMap()
            JSONObject.NULL -> null
            else            -> value
        }
    }
}