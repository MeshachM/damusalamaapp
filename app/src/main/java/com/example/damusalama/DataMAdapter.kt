package com.example.damusalama

import android.content.Context
//import com.example.damusalama.mdata.latitude
//import com.example.damusalama.mdata.longitude
import com.example.damusalama.mdata
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

class DataMAdapter(var context: Context, var mdataArrayList: ArrayList<mdata>) : RecyclerView.Adapter<DataMAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.mitem, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dtmm = mdataArrayList[position]
        holder.paad.text = dtmm.location
        holder.mphone.text = dtmm.n
        holder.namba.text = dtmm.n
        holder.bgr.text = dtmm.group
        //holder.locationmap.setText(dtmm.location);
        holder.donate.setOnClickListener {
            holder.locationmap.visibility = View.VISIBLE
            holder.mcall.visibility = View.VISIBLE
        }
        holder.locationmap.setOnClickListener {
            val intent = Intent(context, LocationActivity::class.java)
            intent.putExtra("latitude", dtmm.latitude)
            intent.putExtra("longitude", dtmm.longitude)
            context.startActivity(intent)
        }
        holder.mcall.setOnClickListener {
            val phoneNo = holder.mphone.text.toString()
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phoneNo")
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mdataArrayList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bgr: TextView
        var paad: TextView
        var namba: TextView
        var mphone: TextView
        var donate: Button
        var mcall: Button
        var locationmap: Button

        init {
            bgr = itemView.findViewById(R.id.bgr)
            namba = itemView.findViewById(R.id.namba)
            mphone = itemView.findViewById(R.id.mphone)
            mcall = itemView.findViewById(R.id.mcall)
            locationmap = itemView.findViewById(R.id.locationmap)
            donate = itemView.findViewById(R.id.donate)
            paad = itemView.findViewById(R.id.paadd)
        }
    }
}