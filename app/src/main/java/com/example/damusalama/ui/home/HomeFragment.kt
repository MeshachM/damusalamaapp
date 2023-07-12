package com.example.damusalama.ui.home

import android.content.Context
//import com.example.damusalama.ui.home.HomeViewModel.text
import androidx.recyclerview.widget.RecyclerView
import com.example.damusalama.data
import com.example.damusalama.DataAdapter
import com.example.damusalama.ui.home.HomeViewModel
import com.example.damusalama.R
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.*
import java.util.ArrayList

class HomeFragment : Fragment() {
    var dataRv: RecyclerView? = null
    var dataArrayList: ArrayList<data>? = null
    var dataAdapter: DataAdapter? = null
    var db: FirebaseFirestore? = null
    var context = null
    var homeViewModel: HomeViewModel? = null

    //private HomeViewModel homeViewModel;
    fun HomeViewModel(root: View) {
        dataRv = root.findViewById(R.id.datarv)
        dataRv?.setHasFixedSize(true)
        dataRv?.setLayoutManager(LinearLayoutManager(getContext()))
        db = FirebaseFirestore.getInstance()
        dataArrayList = ArrayList()
        dataAdapter = DataAdapter(requireContext(), dataArrayList!!)
        dataRv?.setAdapter(dataAdapter)
        EventChangeListener()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView = root.findViewById<TextView>(R.id.text_home)
        homeViewModel!!.text.observe(viewLifecycleOwner) { s -> textView.text = s }
        HomeViewModel(root)
        return root
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
                            dataArrayList!!.add(dc.document.toObject(data::class.java))
                        }
                        dataAdapter!!.notifyDataSetChanged()
                    }
                })
    }
}