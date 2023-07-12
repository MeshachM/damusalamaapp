package com.example.damusalama

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.damusalama.tdata
import com.example.damusalama.DataTechMakeAdapter
import android.os.Bundle
import android.util.Log
import com.example.damusalama.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.*
import java.util.ArrayList

class HomeTechMakeActivity : AppCompatActivity() {
    var tdataRv: RecyclerView? = null
    var tdataArrayList: ArrayList<tdata>? = null
    var tdataAdapter: DataTechMakeAdapter? = null
    var db: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_tech_make)
        tdataRv = findViewById(R.id.tdatarv)
        db = FirebaseFirestore.getInstance()
        tdataRv?.setLayoutManager(LinearLayoutManager(this))
        tdataArrayList = ArrayList()
        tdataAdapter = DataTechMakeAdapter(this, tdataArrayList!!)
        tdataRv?.setAdapter(tdataAdapter)
        EventChangeListener()
    }

    private fun EventChangeListener() {
        db!!.collection("DonorsInfo") /*.orderBy("spinner", Query.Direction.ASCENDING)*/
                .addSnapshotListener(EventListener { value, error ->
                    if (error != null) {
                        Log.e("Firestore error", error.message!!)
                        return@EventListener
                    }
                    for (dc in value!!.documentChanges) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            tdataArrayList!!.add(dc.document.toObject(tdata::class.java))
                        }
                        tdataAdapter!!.notifyDataSetChanged()
                    }
                })
    }
}