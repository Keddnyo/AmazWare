package io.github.keddnyo.amazware

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        val url = "https://schakal.ru/fw/latest.json"
        val okHttpClient = OkHttpClient()

        val request = Request.Builder().url(url).build()
        okHttpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val textView = findViewById<TextView>(R.id.textView)

                runOnUiThread {
                    textView.text = response.body()?.string()
                }
            }
        })
    }
}