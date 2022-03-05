package io.github.keddnyo.amazware.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import io.github.keddnyo.amazware.R
import io.github.keddnyo.amazware.utils.Adapter
import io.github.keddnyo.amazware.utils.Download
import io.github.keddnyo.amazware.utils.Lang

class ExtrasResponse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.extras_response)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = intent.getStringExtra("title") // New Title
    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)
        val responseList = findViewById<ListView>(R.id.responseList)
        val responseField = findViewById<TextView>(R.id.responseField)
        val none = getString(R.string.none)
        val context = this@ExtrasResponse

        val json = intent.getStringExtra("json")

        val firmwareVersion = intent.getStringExtra("firmwareVersion")
        val firmwareMd5 = intent.getStringExtra("firmwareMd5")
        val firmwareUrl = intent.getStringExtra("firmwareUrl")

        val resourceVersion = intent.getStringExtra("resourceVersion")
        val resourceMd5 = intent.getStringExtra("resourceMd5")
        val resourceUrl = intent.getStringExtra("resourceUrl")

        val baseResourceVersion = intent.getStringExtra("baseResourceVersion")
        val baseResourceMd5 = intent.getStringExtra("baseResourceMd5")
        val baseResourceUrl = intent.getStringExtra("baseResourceUrl")

        val fontVersion = intent.getStringExtra("fontVersion")
        val fontMd5 = intent.getStringExtra("fontMd5")
        val fontUrl = intent.getStringExtra("fontUrl")

        val gpsVersion = intent.getStringExtra("gpsVersion")
        val gpsMd5 = intent.getStringExtra("gpsMd5")
        val gpsUrl = intent.getStringExtra("gpsUrl")

        val lang = intent.getStringExtra("lang")

        var changelog = intent.getStringExtra("changelog")

        val list = ArrayList<Adapter>() // Setting response adapter
        val adapter = SimpleAdapter(
            this,
            list,
            android.R.layout.two_line_list_item,
            arrayOf(Adapter.NAME, Adapter.DESCRIPTION),
            intArrayOf(
                android.R.id.text1, android.R.id.text2
            )
        )
        responseList.adapter = adapter

        when (sharedPreferences.getBoolean("simple_response", true)) {
            false -> {
                responseList.visibility = View.GONE
                val response = json?.replace("\\", "")
                responseField.text = response
            }
            true -> {
                if (firmwareVersion != null) {
                    list.add(
                        Adapter(
                            "${getString(R.string.firmware_fw)}: $firmwareVersion",
                            "MD5: $firmwareMd5"
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                } else {
                    list.add(
                        Adapter(
                            getString(R.string.firmware_fw) + ": " + none,
                            "MD5: $none"
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                }
                if (resourceVersion != null) {
                    list.add(
                        Adapter(
                            "${getString(R.string.firmware_res)}: $resourceVersion",
                            "MD5: $resourceMd5"
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                } else {
                    list.add(
                        Adapter(
                            getString(R.string.firmware_res) + ": " + none,
                            "MD5: $none"
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                }
                if (baseResourceVersion != null) {
                    list.add(
                        Adapter(
                            "${getString(R.string.firmware_base_res)}: $baseResourceVersion",
                            "MD5: $baseResourceMd5"
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                } else {
                    list.add(
                        Adapter(
                            getString(R.string.firmware_base_res) + ": " + none,
                            "MD5: $none"
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                }
                if (fontVersion != null) {
                    list.add(
                        Adapter(
                            "${getString(R.string.firmware_ft)}: $fontVersion",
                            "MD5: $fontMd5"
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                } else {
                    list.add(
                        Adapter(
                            getString(R.string.firmware_ft) + ": " + none,
                            "MD5: $none"
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                }
                if (gpsVersion != null) {
                    list.add(
                        Adapter(
                            "${getString(R.string.firmware_gps)}: $gpsVersion",
                            "MD5: $gpsMd5"
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                } else {
                    list.add(
                        Adapter(
                            getString(R.string.firmware_gps) + ": " + none,
                            "MD5: $none"
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                }
                if (lang != null) {
                    val language =
                        Lang().rename(this@ExtrasResponse, lang)

                    list.add(
                        Adapter(
                            "${getString(R.string.firmware_lang)}:",
                            language
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                } else {
                    list.add(
                        Adapter(
                            "${getString(R.string.firmware_lang)}:",
                            none
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                }
                if (changelog != null) {
                    changelog = changelog.substringBefore('#')
                    list.add(
                        Adapter(
                            "${getString(R.string.firmware_change_log)}:",
                            changelog
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                } else {
                    list.add(
                        Adapter(
                            "${getString(R.string.firmware_change_log)}:",
                            none
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                }

                responseList.onItemClickListener =
                    AdapterView.OnItemClickListener { _, _, position, _ ->
                        when (position) {
                            0 -> {
                                if (firmwareUrl != null) {
                                    Download().run(
                                        context,
                                        firmwareUrl
                                    )
                                }
                            }
                            1 -> {
                                if (resourceUrl != null) {
                                    Download().run(
                                        context,
                                        resourceUrl
                                    )
                                }
                            }
                            2 -> {
                                if (baseResourceUrl != null) {
                                    Download().run(
                                        context,
                                        baseResourceUrl
                                    )
                                }
                            }
                            3 -> {
                                if (fontUrl != null) {
                                    Download().run(
                                        context,
                                        fontUrl
                                    )
                                }
                            }
                            4 -> {
                                if (gpsUrl != null) {
                                    Download().run(
                                        context,
                                        gpsUrl
                                    )
                                }
                            }
                        }
                    }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}