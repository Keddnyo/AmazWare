package io.github.keddnyo.amazware.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import io.github.keddnyo.amazware.R

class Adapter(private val context: Context, private val arrayList: java.util.ArrayList<AdapterData>) : BaseAdapter() {

    private lateinit var firmwareIcon: ImageView
    private lateinit var firmwareVersion: TextView
    private lateinit var firmwareDescription: TextView
    override fun getCount(): Int {
        return arrayList.size
    }
    override fun getItem(position: Int): Any {
        return position
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        if (convertView != null) {
            firmwareIcon = convertView.findViewById(R.id.firmwareIcon)
            firmwareVersion = convertView.findViewById(R.id.firmwareVersion)
            firmwareDescription = convertView.findViewById(R.id.firmwareDescription)
            firmwareIcon.setImageDrawable(arrayList[position].firmwareIcon)
            firmwareVersion.text = arrayList[position].firmwareVersion
            firmwareDescription.text = arrayList[position].firmwareDescription
        }
        return convertView
    }
}