package com.example.damusalama

import android.content.Context
import com.example.damusalama.qdata
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.damusalama.R
import android.widget.TextView
import java.util.ArrayList

//import com.example.damusalama.ui.home.HomeViewModel;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
class DataQAdapter     //  FusedLocationProviderClient mFusedLocationClient;
(var context: Context, var qdataArrayList: ArrayList<qdata>) : RecyclerView.Adapter<DataQAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.message, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dq = qdataArrayList[position]
        holder.swali.text = dq.swali
        holder.jibu.text = dq.jib
    }

    override fun getItemCount(): Int {
        return qdataArrayList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var swali: TextView
        var jibu: TextView

        init {
            swali = itemView.findViewById(R.id.swali)
            jibu = itemView.findViewById(R.id.jibu)
        }
    }

    companion object {
        private const val REQUEST_LOCATION = 5
    }
}