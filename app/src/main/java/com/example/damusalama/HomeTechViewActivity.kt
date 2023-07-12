package com.example.damusalama

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.damusalama.tmdata
import com.example.damusalama.DataTechViewAdapter
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.damusalama.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.*
import java.util.ArrayList

class HomeTechViewActivity : AppCompatActivity() {
    var tcall: Button? = null
    var tdonate: Button? = null
    var tdatamv: RecyclerView? = null
    var tmdataArrayList: ArrayList<tmdata>? = null
    var tdataAdapterm: DataTechViewAdapter? = null
    var db: FirebaseFirestore? = null
    var bloodGroup = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_tech_view)
        tdatamv = findViewById(R.id.tdatarv)
        tdatamv?.setHasFixedSize(true)
        tdatamv?.setLayoutManager(LinearLayoutManager(this))
        db = FirebaseFirestore.getInstance()
        tmdataArrayList = ArrayList()
        tdataAdapterm = DataTechViewAdapter(this, tmdataArrayList!!)
        tdatamv?.setAdapter(tdataAdapterm)
        bloodGroup.add("A+")
        bloodGroup.add("AB+")
        bloodGroup.add("B+")
        bloodGroup.add("O+")
        bloodGroup.add("A-")
        bloodGroup.add("AB-")
        bloodGroup.add("B-")
        bloodGroup.add("O-")
        EventChangeListener()
    }

    private fun EventChangeListener() {
        for (b in bloodGroup) {
            db!!.collection("Request").document("Patient").collection(b) /*.orderBy("spinner", Query.Direction.ASCENDING)*/
                    .addSnapshotListener(EventListener { value, error ->
                        if (error != null) {
                            Log.e("Firestore error", error.message!!)
                            return@EventListener
                        }
                        for (dc in value!!.documentChanges) {
                            if (dc.type == DocumentChange.Type.ADDED) {
                                tmdataArrayList!!.add(dc.document.toObject(tmdata::class.java))
                                Log.d("datas, ", dc.toString())
                            }
                            tdataAdapterm!!.notifyDataSetChanged()
                        }
                    })
        }
    }
}