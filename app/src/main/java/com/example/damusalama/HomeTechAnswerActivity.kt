package com.example.damusalama

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.damusalama.adata
import com.example.damusalama.DataAAdapter
import android.os.Bundle
import android.util.Log
import com.example.damusalama.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.*
import java.util.ArrayList

class HomeTechAnswerActivity : AppCompatActivity() {
    var adataRv: RecyclerView? = null
    var adataArrayList: ArrayList<adata>? = null
    var dataAAdapter: DataAAdapter? = null
    var db: FirebaseFirestore? = null
    var context: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_tech_answer)
        adataRv = findViewById(R.id.adatarv)
        adataRv?.setHasFixedSize(true)
        adataRv?.setLayoutManager(LinearLayoutManager(this))
        db = FirebaseFirestore.getInstance()
        adataArrayList = ArrayList()
        dataAAdapter = DataAAdapter(this, adataArrayList!!)
        adataRv?.setAdapter(dataAAdapter)
        EventChangeListener()
    }

    private fun EventChangeListener() {
        db!!.collection("Answers").whereEqualTo("jib", "?") /*.orderBy("spinner", Query.Direction.ASCENDING)*/
                .addSnapshotListener(EventListener { value, error ->
                    if (error != null) {
                        Log.e("Firestore error", error.message!!)
                        return@EventListener
                    }
                    for (dc in value!!.documentChanges) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            adataArrayList!!.add(dc.document.toObject(adata::class.java))
                        }
                        dataAAdapter!!.notifyDataSetChanged()
                    }
                })
    }
}