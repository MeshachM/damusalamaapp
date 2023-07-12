package com.example.damusalama

import android.content.Context
import com.example.damusalama.tdata
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
class DataTechMakeAdapter     //  FusedLocationProviderClient mFusedLocationClient;
(var context: Context, var tdataArrayList: ArrayList<tdata>) : RecyclerView.Adapter<DataTechMakeAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.titem, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dt = tdataArrayList[position]
        holder.tbtl.text = dt.spinner
        holder.tphn.text = dt.number
        holder.taddr.text = dt.dadd
        holder.tphone.text = dt.number
        holder.tbutoreq.setOnClickListener {
            val bg = dt.spinner
            val phone = dt.number
            val location = dt.dadd
            val i = Intent(context, CaptureLocation::class.java)
            i.putExtra("group", bg)
            i.putExtra("phone", phone)
            i.putExtra("location", location)
            context.startActivity(i)
        }

        holder.tcadono.setOnClickListener {
            val phoneNo = holder.tphone.text.toString()
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
        return tdataArrayList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tbtl: TextView
        var tphn: TextView
        var taddr: TextView
        var tphone: TextView
        var tbutoreq: Button
        var tcadono: Button

        init {
            tbutoreq = itemView.findViewById(R.id.tbutreq)
            tcadono = itemView.findViewById(R.id.tcadono)
            tbtl = itemView.findViewById(R.id.tbtl)
            tphn = itemView.findViewById(R.id.tphn)
            tphone = itemView.findViewById(R.id.tphone)
            taddr = itemView.findViewById(R.id.taddr)
            tbutoreq.setOnClickListener { }
            tcadono.setOnClickListener { }
        }
    }

    companion object {
        private const val REQUEST_LOCATION = 5
    }
}