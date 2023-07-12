package com.example.damusalama

import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.os.Bundle
import com.example.damusalama.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import android.widget.Toast
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.Button
import com.example.damusalama.HomeTActivity
import java.util.*

class RTechnicianActivity : AppCompatActivity() {
    var regintbtn: Button? = null
    var nm: String? = null
    var em: String? = null
    var num: String? = null
    var id: String? = null
    var cn: String? = null
    var name: EditText? = null
    var email: EditText? = null
    var password: EditText? = null
    var confirm: EditText? = null
    var number: EditText? = null
    var idno: EditText? = null
    var cname: EditText? = null
    private var mAuth: FirebaseAuth? = null
    private var db: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_doctor)
        regintbtn = findViewById(R.id.regintbtn)
        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        confirm = findViewById(R.id.confirm)
        number = findViewById(R.id.number)
        idno = findViewById(R.id.idno)
        cname = findViewById(R.id.cname)
        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        regintbtn?.setOnClickListener(View.OnClickListener { fields })
    }

    private val fields: Unit
        private get() {
            if (!TextUtils.isEmpty(email!!.text.toString()) || !TextUtils.isEmpty(idno!!.text.toString()) || !TextUtils.isEmpty(number!!.text.toString()) || !TextUtils.isEmpty(name!!.text.toString())
                    || !TextUtils.isEmpty(password!!.text.toString()) || !TextUtils.isEmpty(cname!!.text.toString()) || !TextUtils.isEmpty(confirm!!.text.toString())) {
                if (password!!.text.toString() == confirm!!.text.toString()) {
                    mAuth!!.createUserWithEmailAndPassword(email!!.text.toString(), password!!.text.toString()).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            sendtodb()
                        } else {
                            Toast.makeText(this@RTechnicianActivity, "Confirm Password and Password Field doesn't match.", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

    fun sendtodb() {
//        db.collection("TechnicianProfile").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        nm = name.getText().toString();
//                        em = email.getText().toString();
//                        id = idno.getText().toString();
//                        cn = cname.getText().toString();
//                        num = number.getText().toString();
//                    } else {
        nm = name!!.text.toString()
        em = email!!.text.toString()
        num = number!!.text.toString()
        cn = cname!!.text.toString()
        id = idno!!.text.toString()
        val details = HashMap<String, String>()
        details["name"] = nm!!
        details["email"] = em!!
        details["cname"] = cn!!
        details["number"] = num!!
        details["idno"] = id!!
        Objects.requireNonNull(FirebaseAuth.getInstance().uid)?.let {
            db!!.collection("Profile").document(it).set(details).addOnCompleteListener {
            val Home = Intent(this@RTechnicianActivity, HomeTActivity::class.java)
            Home.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(Home)
        }
        }
        name!!.setText("")
        email!!.setText("")
        number!!.setText("")
        cname!!.setText("")
        idno!!.setText("")
        password!!.setText("")
        confirm!!.setText("")
        //                    }
//                }
//            }
//        });
    }
}