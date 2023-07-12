package com.example.damusalama.ui.dashboard

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.damusalama.mdata
import com.example.damusalama.DataMAdapter
import com.example.damusalama.ui.dashboard.DashboardViewModel
import com.example.damusalama.R
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.damusalama.Const
import com.google.firebase.firestore.*
import java.util.ArrayList
//import com.example.damusalama.ui.home.DashboardViewModel;
//import com.example.damusalama.mdataMAdapter;


class DashboardFragment : Fragment() {
    var call: Button? = null
    var donate: Button? = null
    var datamv: RecyclerView? = null
    var mdataArrayList: ArrayList<mdata>? = null
    var dataAdapterm: DataMAdapter? = null
    var db: FirebaseFirestore? = null
    var context = null
    lateinit var DashboardViewModel: DashboardViewModel

    //private Object mdataMAdapter;
    //private DashboardViewModel DashboardViewModel;
    fun DashboardViewModel(root: View) {
        datamv = root.findViewById(R.id.datamv)
        datamv?.setHasFixedSize(true)
        datamv?.setLayoutManager(LinearLayoutManager(getContext()))
        db = FirebaseFirestore.getInstance()
        mdataArrayList = ArrayList()
        dataAdapterm = DataMAdapter(requireContext(), mdataArrayList!!)
        datamv?.setAdapter(dataAdapterm)
        EventChangeListener()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        DashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView = root.findViewById<TextView>(R.id.text_dashboard)
        DashboardViewModel!!.text.observe(viewLifecycleOwner) { s -> textView.text = s }
        DashboardViewModel(root)
        return root
    }

    private fun EventChangeListener() {
        db!!.collection("Request").document("Patient").collection(Const.bloodGroup!!) /*.orderBy("spinner", Query.Direction.ASCENDING)*/
                .addSnapshotListener(EventListener { value, error ->
                    if (error != null) {
                        Log.e("Firestore error", error.message!!)
                        return@EventListener
                    }
                    for (dc in value!!.documentChanges) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            mdataArrayList!!.add(dc.document.toObject(mdata::class.java))
                            Log.d("datas, ", dc.toString())
                        }
                        dataAdapterm!!.notifyDataSetChanged()
                    }
                })
    }

}
