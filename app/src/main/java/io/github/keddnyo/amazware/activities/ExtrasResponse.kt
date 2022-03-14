package io.github.keddnyo.amazware.activities

import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import io.github.keddnyo.amazware.R
import io.github.keddnyo.amazware.utils.*

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
        val responseList: ListView = findViewById(R.id.responseList)
        val responseField: TextView = findViewById(R.id.responseField)
        val none = getString(R.string.none)
        val context = this@ExtrasResponse

        lateinit var listView: ListView
        val arrayList: ArrayList<AdapterData> = ArrayList()
        val adapter: android.widget.Adapter?
        adapter = Adapter(this, arrayList)
        listView.adapter = adapter

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

        when (sharedPreferences.getBoolean("simple_response", true)) {
            false -> {
                responseList.visibility = View.GONE
                val response = json?.replace("\\", "")
                responseField.text = response
            }
            true -> {
                if (firmwareVersion != null) {
                    arrayList.add(
                        AdapterData(
                            getDrawable(R.drawable.ic_extras),
                            "${getString(R.string.firmware_fw)} $firmwareMd5",
                            "MD5: $firmwareMd5"
                        )
                    )
                    (adapter as io.github.keddnyo.amazware.utils.Adapter).notifyDataSetChanged()
                } else {
                    arrayList.add(
                        AdapterData(
                            getDrawable(R.drawable.ic_extras),
                            "${getString(R.string.firmware_fw)} $none",
                            "MD5: $none"
                        )
                    )
                    (adapter as io.github.keddnyo.amazware.utils.Adapter).notifyDataSetChanged()
                }
                if (resourceVersion != null) {
                    arrayList.add(
                        AdapterData(
                            getDrawable(R.drawable.ic_extras),
                            "${getString(R.string.firmware_res)} $resourceVersion",
                            "MD5: $resourceMd5"
                        )
                    )
                    (adapter as io.github.keddnyo.amazware.utils.Adapter).notifyDataSetChanged()
                } else {
                    arrayList.add(
                        AdapterData(
                            getDrawable(R.drawable.ic_extras),
                            "${getString(R.string.firmware_res)} $none",
                            "MD5: $none"
                        )
                    )
                    (adapter as io.github.keddnyo.amazware.utils.Adapter).notifyDataSetChanged()
                }
                if (baseResourceVersion != null) {
                    arrayList.add(
                        AdapterData(
                            getDrawable(R.drawable.ic_extras),
                            "${getString(R.string.firmware_base_res)} $baseResourceVersion",
                            "MD5: $baseResourceMd5"
                        )
                    )
                    (adapter as io.github.keddnyo.amazware.utils.Adapter).notifyDataSetChanged()
                } else {
                    arrayList.add(
                        AdapterData(
                            getDrawable(R.drawable.ic_extras),
                            "${getString(R.string.firmware_base_res)} $none",
                            "MD5: $none"
                        )
                    )
                    (adapter as io.github.keddnyo.amazware.utils.Adapter).notifyDataSetChanged()
                }
                if (fontVersion != null) {
                    arrayList.add(
                        AdapterData(
                            getDrawable(R.drawable.ic_extras),
                            "${getString(R.string.firmware_ft)} $fontVersion",
                            "MD5: $fontMd5"
                        )
                    )
                    (adapter as io.github.keddnyo.amazware.utils.Adapter).notifyDataSetChanged()
                } else {
                    arrayList.add(
                        AdapterData(
                            getDrawable(R.drawable.ic_extras),
                            "${getString(R.string.firmware_ft)} $none",
                            "MD5: $none"
                        )
                    )
                    (adapter as io.github.keddnyo.amazware.utils.Adapter).notifyDataSetChanged()
                }
                if (gpsVersion != null) {
                    arrayList.add(
                        AdapterData(
                            getDrawable(R.drawable.ic_extras),
                            "${getString(R.string.firmware_gps)} $gpsVersion",
                            "MD5: $gpsMd5"
                        )
                    )
                    (adapter as io.github.keddnyo.amazware.utils.Adapter).notifyDataSetChanged()
                } else {
                    arrayList.add(
                        AdapterData(
                            getDrawable(R.drawable.ic_extras),
                            "${getString(R.string.firmware_gps)} $none",
                            "MD5: $none"
                        )
                    )
                    (adapter as io.github.keddnyo.amazware.utils.Adapter).notifyDataSetChanged()
                }
                if (lang != null) {
                    val language =
                        Lang().rename(this@ExtrasResponse, lang)

                    arrayList.add(
                        AdapterData(
                            getDrawable(R.drawable.ic_extras),
                            "${getString(R.string.firmware_lang)}:",
                            language
                        )
                    )
                    (adapter as io.github.keddnyo.amazware.utils.Adapter).notifyDataSetChanged()
                } else {
                    arrayList.add(
                        AdapterData(
                            getDrawable(R.drawable.ic_extras),
                            "${getString(R.string.firmware_lang)}:",
                            none
                        )
                    )
                    (adapter as io.github.keddnyo.amazware.utils.Adapter).notifyDataSetChanged()
                }
                if (changelog != null) {
                    changelog = changelog.substringBefore('#')

                    arrayList.add(
                        AdapterData(
                            getDrawable(R.drawable.ic_extras),
                            "${getString(R.string.firmware_change_log)}:",
                            changelog
                        )
                    )
                    (adapter as io.github.keddnyo.amazware.utils.Adapter).notifyDataSetChanged()
                } else {
                    arrayList.add(
                        AdapterData(
                            getDrawable(R.drawable.ic_extras),
                            "${getString(R.string.firmware_change_log)}:",
                            none
                        )
                    )
                    (adapter as io.github.keddnyo.amazware.utils.Adapter).notifyDataSetChanged()
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