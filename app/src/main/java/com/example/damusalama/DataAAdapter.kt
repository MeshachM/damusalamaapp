package com.example.damusalama

import android.content.Context
import com.example.damusalama.adata
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.example.damusalama.R
import com.google.android.gms.tasks.OnCompleteListener
import android.widget.TextView
import android.widget.EditText
import androidx.cardview.widget.CardView
import com.google.android.gms.tasks.Task
import java.util.ArrayList
import java.util.HashMap

//import com.example.damusalama.ui.home.HomeViewModel;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
class DataAAdapter     //  FusedLocationProviderClient mFusedLocationClient;
(var context: Context, var adataArrayList: ArrayList<adata>) : RecyclerView.Adapter<DataAAdapter.MyViewHolder>() {
    var db: FirebaseFirestore? = null
    var answer: String? = null
    var qn: String? = null
    private val Random: String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.answers, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dq = adataArrayList[position]
        holder.swali.text = dq.swali
        holder.jibu.text = dq.jib
        holder.submitans.setOnClickListener {
            sendtodb(holder, db)
            // holder.answercard.setVisibility(View.GONE);
        }
    }

    fun sendtodb(holder: MyViewHolder, db: FirebaseFirestore?) {
        var db = db
        answer = holder.jib.text.toString()
        qn = holder.swali.text.toString()
        val details = HashMap<String, String>()
        details["jib"] = answer!!
        details["swali"] = qn!!
        db = FirebaseFirestore.getInstance()
        val finalDb: FirebaseFirestore = db
        db.collection("Answers") /*.document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()+Random)).set(details)*/
                .document("Answered").collection("Answers").add(details).addOnCompleteListener(object : OnCompleteListener<Any?> {
                    override fun onComplete(p0: Task<Any?>) {
                        holder.jib.setText("")
                    } //            @Override
                    //            public void onComplete(@NonNull Task<Void> task) {
                    //            }
                })
    }

    override fun getItemCount(): Int {
        return adataArrayList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var swali: TextView
        var jibu: TextView
        var jib: EditText
        var submitans: Button
        var answercard: CardView
        var answer: String? = null
        var qn: String? = null

        init {
            swali = itemView.findViewById(R.id.swali)
            jibu = itemView.findViewById(R.id.jibu)
            submitans = itemView.findViewById(R.id.submitans)
            jib = itemView.findViewById(R.id.answer)
            answercard = itemView.findViewById(R.id.answercard)
        }
    }

    companion object {
        private const val REQUEST_LOCATION = 5
    }


    fun <TResult> Task<TResult>.addOnCompleteListener(onCompleteListener: OnCompleteListener<Any?>) {

    }
}
