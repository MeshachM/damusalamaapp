package com.example.damusalama

import android.content.Context
//import com.example.damusalama.tmdata.latitude
//import com.example.damusalama.tmdata.longitude
import com.example.damusalama.tmdata
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.example.damusalama.R
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Button
import com.example.damusalama.LocationActivity
import android.widget.TextView
import java.util.ArrayList

class DataTechViewAdapter(var context: Context, var tmdataArrayList: ArrayList<tmdata>) : RecyclerView.Adapter<DataTechViewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.tmitem, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dtmm = tmdataArrayList[position]
        holder.tmpaad.text = dtmm.location
        holder.tmmphone.text = dtmm.n
        holder.tmnamba.text = dtmm.n
        holder.tmbgr.text = dtmm.group
        //holder.locationmap.setText(dtmm.location);
        holder.tmdonate.setOnClickListener {
          //  holder.tmlocationmap.visibility = View.VISIBLE
            holder.tmmcall.visibility = View.VISIBLE
        }
//        holder.tmlocationmap.setOnClickListener {
//            val intent = Intent(context, LocationActivity::class.java)
//            intent.putExtra("latitude", dtmm.latitude)
//            intent.putExtra("longitude", dtmm.longitude)
//            context.startActivity(intent)
//        }
        holder.tmmcall.setOnClickListener {
            val phoneNo = holder.tmmphone.text.toString()
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phoneNo")
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return tmdataArrayList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tmbgr: TextView
        var tmpaad: TextView
        var tmnamba: TextView
        var tmmphone: TextView
        var tmdonate: Button
        var tmmcall: Button
       // var tmlocationmap: Button

        init {
            tmbgr = itemView.findViewById(R.id.tmbgr)
            tmnamba = itemView.findViewById(R.id.tmnamba)
            tmmphone = itemView.findViewById(R.id.tmmphone)
            tmmcall = itemView.findViewById(R.id.tmmcall)
          //  tmlocationmap = itemView.findViewById(R.id.tmlocationmap)
            tmdonate = itemView.findViewById(R.id.tmdonate)
            tmpaad = itemView.findViewById(R.id.tmpaadd)
        }
    }
}