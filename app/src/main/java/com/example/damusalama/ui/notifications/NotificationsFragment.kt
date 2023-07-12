package com.example.damusalama.ui.notifications

import android.content.Context
//import com.example.damusalama.ui.notifications.NotificationsViewModel.text
import androidx.recyclerview.widget.RecyclerView
import com.example.damusalama.qdata
import com.example.damusalama.DataQAdapter
import android.widget.EditText
import com.example.damusalama.ui.notifications.NotificationsViewModel
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
import com.google.android.gms.tasks.OnCompleteListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import java.util.ArrayList
import java.util.HashMap

class NotificationsFragment : Fragment() {
    var qdataRv: RecyclerView? = null
    var qdataArrayList: ArrayList<qdata>? = null
    var dataQAdapter: DataQAdapter? = null
    var db: FirebaseFirestore? = null
    var submitqn: Button? = null
    var qn: String? = null
    var swali: EditText? = null
    var context = null
    var notificationsViewModel: NotificationsViewModel? = null
    private val Random: Any? = null
    fun NotificationsViewModel(root: View) {
        qdataRv = root.findViewById(R.id.qdatarv)
        qdataRv?.setHasFixedSize(true)
        qdataRv?.setLayoutManager(LinearLayoutManager(getContext()))
        db = FirebaseFirestore.getInstance()
        qdataArrayList = ArrayList()
        dataQAdapter = DataQAdapter(requireContext(), qdataArrayList!!)
        qdataRv?.setAdapter(dataQAdapter)
        submitqn = root.findViewById(R.id.submitqn)
        swali = root.findViewById(R.id.qn)
        EventChangeListener()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView = root.findViewById<TextView>(R.id.text_notifications)
        notificationsViewModel!!.text.observe(viewLifecycleOwner) { s -> textView.text = s }
        NotificationsViewModel(root)
        return root
    }

    private fun EventChangeListener() {
        db!!.collection("Answers").document("Answered").collection("Answers") /*.orderBy("spinner", Query.Direction.ASCENDING)*/
                .addSnapshotListener(EventListener { value, error ->
                    if (error != null) {
                        Log.e("Firestore error", error.message!!)
                        return@EventListener
                    }
                    for (dc in value!!.documentChanges) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            qdataArrayList!!.add(dc.document.toObject(qdata::class.java))
                        }
                        dataQAdapter!!.notifyDataSetChanged()
                    }
                })
        submitqn!!.setOnClickListener { sendtodb() }
    }

    fun sendtodb() {
//        db.collection("DonorsInfo").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        dn = dname.getText().toString();
//                        id = idno.getText().toString();
//                        num = number.getText().toString();
//                        dad = dadd.getText().toString();
//                        dt= date.getText().toString();
//                        sp= spinner.getSelectedItem().toString();
//                    } else {
        qn = swali!!.text.toString()
        val details = HashMap<String, String>()
        details["swali"] = qn!!
        details["jib"] = "?"
        db!!.collection("Answers").add(details).addOnCompleteListener(object : OnCompleteListener<Any?> {
            override fun onComplete(p0: Task<Any?>) {
                swali!!.setText("")
                Toast.makeText(getContext(), "Question sent, Please wait for an answer", Toast.LENGTH_LONG).show()
            }
        })
        //                    }
//                }
//            }
//        });
    }

    private fun <TResult> Task<TResult>.addOnCompleteListener(onCompleteListener: Any) {
        TODO("Not yet implemented")
    }
}
