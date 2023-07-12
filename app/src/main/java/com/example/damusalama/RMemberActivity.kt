package com.example.damusalama

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.os.Bundle
import com.example.damusalama.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.example.damusalama.HomeMActivity
import java.util.*

class RMemberActivity : AppCompatActivity() {
    var reginmbtn: Button? = null
    var nm: String? = null
    var em: String? = null
    var pass: String? = null
    var sp: String? = null
    var num: String? = null
    var id: String? = null
    var add: String? = null
    var spinner: Spinner? = null
    var name: EditText? = null
    var email: EditText? = null
    var password: EditText? = null
    var confirm: EditText? = null
    var number: EditText? = null
    var idno: EditText? = null
    var address: EditText? = null
    private var mAuth: FirebaseAuth? = null
    private var db: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_r_member)
        reginmbtn = findViewById(R.id.reginmbtn)
        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        confirm = findViewById(R.id.confirm)
        number = findViewById(R.id.number)
        idno = findViewById(R.id.idno)
        address = findViewById(R.id.address)
        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        spinner = findViewById(R.id.spinner)
        val staticSpinner = findViewById<View>(R.id.spinner) as Spinner
        // Create an ArrayAdapter using the string array and a default spinner
        val staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.Blood_Groups,
                        android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        staticSpinner.adapter = staticAdapter
        staticSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View,
                                        position: Int, id: Long) {
                Log.v("item", (parent.getItemAtPosition(position) as String))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }
        reginmbtn?.setOnClickListener(View.OnClickListener { fields })
    }

    private val fields: Unit
        private get() {
            if (!TextUtils.isEmpty(email!!.text.toString()) || !TextUtils.isEmpty(idno!!.text.toString()) || !TextUtils.isEmpty(number!!.text.toString()) || !TextUtils.isEmpty(name!!.text.toString())
                    || !TextUtils.isEmpty(password!!.text.toString()) || !TextUtils.isEmpty(address!!.text.toString()) || !TextUtils.isEmpty(confirm!!.text.toString())) {
                if (password!!.text.toString() == confirm!!.text.toString()) {
                    mAuth!!.createUserWithEmailAndPassword(email!!.text.toString(), password!!.text.toString()).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            sendtodb()
                        } else {
                            Toast.makeText(this@RMemberActivity, "Confirm Password and Password Field doesn't match.", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

    fun sendtodb() {
//        db.collection("MemberProfile").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        nm = name.getText().toString();
//                        em = email.getText().toString();
//                        id = idno.getText().toString();
//                        add = address.getText().toString();
//                        num = number.getText().toString();
//                    } else {
        nm = name!!.text.toString()
        em = email!!.text.toString()
        num = number!!.text.toString()
        add = address!!.text.toString()
        id = idno!!.text.toString()
        sp = spinner!!.selectedItem.toString()
        val details = HashMap<String, String>()
        details["name"] = nm!!
        details["email"] = em!!
        details["address"] = add!!
        details["number"] = num!!
        details["idno"] = id!!
        details["spinner"] = sp!!
        Objects.requireNonNull(FirebaseAuth.getInstance().uid)?.let {
            db!!.collection("Profile").document(it).set(details).addOnCompleteListener {
            val homemember = Intent(this@RMemberActivity, HomeMActivity::class.java)
            homemember.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(homemember)
        }
        }
    }
}