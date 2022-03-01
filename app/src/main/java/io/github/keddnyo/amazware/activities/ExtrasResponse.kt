package io.github.keddnyo.amazware.activities

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import io.github.keddnyo.amazware.R
import io.github.keddnyo.amazware.utils.Adapter
import io.github.keddnyo.amazware.utils.Download
import io.github.keddnyo.amazware.utils.Lang

class ExtrasResponse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extras_response)

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
                responseField.text = json
            }
            true -> {
                if (firmwareVersion != null) {
                    list.add(
                        Adapter(
                            "${getString(R.string.firmware_version)}: $firmwareVersion",
                            "MD5: $firmwareMd5"
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                } else {
                    list.add(
                        Adapter(
                            getString(R.string.firmware_version) + ": " + none,
                            "MD5: $none"
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                }
                if (resourceVersion != null) {
                    list.add(
                        Adapter(
                            "${getString(R.string.resource_version)}: $resourceVersion",
                            "MD5: $resourceMd5"
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                } else {
                    list.add(
                        Adapter(
                            getString(R.string.resource_version) + ": " + none,
                            "MD5: $none"
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                }
                if (baseResourceVersion != null) {
                    list.add(
                        Adapter(
                            "${getString(R.string.base_resource_version)}: $baseResourceVersion",
                            "MD5: $baseResourceMd5"
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                } else {
                    list.add(
                        Adapter(
                            getString(R.string.base_resource_version) + ": " + none,
                            "MD5: $none"
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                }
                if (fontVersion != null) {
                    list.add(
                        Adapter(
                            "${getString(R.string.font_version)}: $fontVersion",
                            "MD5: $fontMd5"
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                } else {
                    list.add(
                        Adapter(
                            getString(R.string.font_version) + ": " + none,
                            "MD5: $none"
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                }
                if (gpsVersion != null) {
                    list.add(
                        Adapter(
                            "${getString(R.string.gps_version)}: $gpsVersion",
                            "MD5: $gpsMd5"
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                } else {
                    list.add(
                        Adapter(
                            getString(R.string.gps_version) + ": " + none,
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
                            "${getString(R.string.lang)}:",
                            language
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                } else {
                    list.add(
                        Adapter(
                            "${getString(R.string.lang)}:",
                            none
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                }
                if (changelog != null) {
                    changelog = changelog.substringBefore('#')
                    list.add(
                        Adapter(
                            "${getString(R.string.change_log)}:",
                            changelog
                        )
                    )
                    adapter.notifyDataSetChanged() // Commit changes
                } else {
                    list.add(
                        Adapter(
                            "${getString(R.string.change_log)}:",
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
                                        firmwareUrl,
                                        "firmware",
                                        "?"
                                    )
                                } else {
                                    Toast.makeText(
                                        context,
                                        R.string.none,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            1 -> {
                                if (resourceUrl != null) {
                                    Download().run(
                                        context,
                                        resourceUrl,
                                        "resource",
                                        "?"
                                    )
                                } else {
                                    Toast.makeText(
                                        context,
                                        R.string.none,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            2 -> {
                                if (baseResourceUrl != null) {
                                    Download().run(
                                        context,
                                        baseResourceUrl,
                                        "base_resource",
                                        "?"
                                    )
                                } else {
                                    Toast.makeText(
                                        context,
                                        R.string.none,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            3 -> {
                                if (fontUrl != null) {
                                    Download().run(
                                        context,
                                        fontUrl,
                                        "font",
                                        "?"
                                    )
                                } else {
                                    Toast.makeText(
                                        context,
                                        R.string.none,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            4 -> {
                                if (gpsUrl != null) {
                                    Download().run(
                                        context,
                                        gpsUrl,
                                        "gps",
                                        "?"
                                    )
                                } else {
                                    Toast.makeText(
                                        context,
                                        R.string.none,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            5 -> {
                                if (lang != null) {
                                    Toast.makeText(context, lang, Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        R.string.none,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
            }
        }

    }
}