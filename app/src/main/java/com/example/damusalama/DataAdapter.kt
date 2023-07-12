package com.example.damusalama

import android.content.Context
import com.example.damusalama.data
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.example.damusalama.R
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Button
import com.example.damusalama.CaptureLocation
import android.widget.TextView
import java.util.ArrayList

//import com.example.damusalama.ui.home.HomeViewModel;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
class DataAdapter     //  FusedLocationProviderClient mFusedLocationClient;
(var context: Context, var dataArrayList: ArrayList<data>) : RecyclerView.Adapter<DataAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dt = dataArrayList[position]
        holder.btl.text = dt.spinner
        holder.phn.text = dt.number
        holder.addr.text = dt.dadd
        holder.phone.text = dt.number
        holder.butoreq.setOnClickListener {
            val bg = dt.spinner
            val phone = dt.number
            val location = dt.dadd
            val i = Intent(context, CaptureLocation::class.java)
            i.putExtra("group", bg)
            i.putExtra("phone", phone)
            i.putExtra("location", location)
            context.startActivity(i)
        }
        holder.cadono.setOnClickListener {
            val phoneNo = holder.phone.text.toString()
            //                   if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
//                   {
//                       ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.CALL_PHONE},101);
//                   }
//                   else {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phoneNo")
            context.startActivity(intent)
            //            phoneNumber = "";
//            finish();
//                   }
//                   Toast.makeText(context,"finish",Toast.LENGTH_LONG).show();
        }
    }

    override fun getItemCount(): Int {
        return dataArrayList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var btl: TextView
        var phn: TextView
        var addr: TextView
        var phone: TextView
        var butoreq: Button
        var cadono: Button

        init {
            butoreq = itemView.findViewById(R.id.butreq)
            cadono = itemView.findViewById(R.id.cadono)
            btl = itemView.findViewById(R.id.btl)
            phn = itemView.findViewById(R.id.phn)
            phone = itemView.findViewById(R.id.phone)
            addr = itemView.findViewById(R.id.addr)
            butoreq.setOnClickListener { }
            cadono.setOnClickListener { }
        }
    }

    companion object {
        private const val REQUEST_LOCATION = 5
    }
}