package io.github.keddnyo.amazware.activities

import android.os.Bundle
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import io.github.keddnyo.amazware.R
import io.github.keddnyo.amazware.utils.Adapter

class ExtrasResponse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extras_response)
    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)
        val responseList = findViewById<ListView>(R.id.responseList)
        val responseField = findViewById<TextView>(R.id.responseField)
        val none = getString(R.string.none)

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

        val changelog = intent.getStringExtra("changelog")

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
            true -> {
                responseField.text = json
            }
            false -> {
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
            }
        }

    }
}