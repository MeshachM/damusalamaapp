package com.example.damusalama

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.os.Bundle
import com.example.damusalama.R
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.Button
import com.example.damusalama.RMemberActivity
import com.example.damusalama.RTechnicianActivity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import android.widget.Toast
import com.example.damusalama.HomeTActivity
import com.example.damusalama.HomeMActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import java.lang.Exception
import java.util.*

class StartActivity : AppCompatActivity() {

    var regtbtn: Button? = null
    var regmbtn: Button? = null
    var logbtn: Button? = null
    var emailtv: TextView? = null
    var passwordtv: TextView? = null
    var progressbar: ProgressBar? = null
    private var mAuth: FirebaseAuth? = null
    private var db: FirebaseFirestore? = null
    var user: String? = null
    var action: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        db = FirebaseFirestore.getInstance()
        try {
            action = intent.getStringExtra("action")
            if (action == "logout") {
                try {
                    FirebaseAuth.getInstance().signOut()
                } catch (e: Exception) {

                }
            }
        } catch (e: Exception) {

        }
        mAuth = FirebaseAuth.getInstance()
        progressbar = findViewById(R.id.progressBar)
        emailtv = findViewById(R.id.emailTextView)
        passwordtv = findViewById(R.id.passwordTextView)
        regmbtn = findViewById(R.id.regmbtn)
        regtbtn = findViewById(R.id.regtnbtn)
        logbtn = findViewById(R.id.loginbtn)

        try {
            user = mAuth!!.currentUser!!.uid
        } catch (e: Exception) {
        }
        if (user == null) {
            regmbtn?.setOnClickListener(View.OnClickListener { startActivity(Intent(this@StartActivity, RMemberActivity::class.java)) })
            regtbtn?.setOnClickListener(View.OnClickListener { startActivity(Intent(this@StartActivity, RTechnicianActivity::class.java)) })
            logbtn?.setOnClickListener(View.OnClickListener { loginUser() })
        } else {
            Objects.requireNonNull(FirebaseAuth.getInstance().uid)?.let {
                db!!.collection("Profile").document(it).addSnapshotListener { value, error ->
                    val cname: String
                    try {
                        assert(value != null)
                        cname = Objects.requireNonNull(value!!["cname"]).toString()
                        Toast.makeText(applicationContext, "Login successful!!", Toast.LENGTH_LONG).show()
                        // hide the progress bar
                        progressbar?.setVisibility(View.GONE)
                        // if sign-in is successful
                        // intent to home activity
                        val intent = Intent(this@StartActivity, HomeTActivity::class.java)
                        startActivity(intent)
                        finish()
                    } catch (e: Exception) {
                        Toast.makeText(applicationContext, "Login successful!!", Toast.LENGTH_LONG).show()
                        // hide the progress bar
                        progressbar?.setVisibility(View.GONE)

                        // if sign-in is successful
                        // intent to home activity
                        val intent = Intent(this@StartActivity, HomeMActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    private fun loginUser() {

        // show the visibility of progress bar to show loading
        progressbar!!.visibility = View.VISIBLE

        // Take the value of two edit texts in Strings
        val email: String
        val password: String
        email = emailtv!!.text.toString()
        password = passwordtv!!.text.toString()

        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext,
                    "Please enter email!!",
                    Toast.LENGTH_LONG)
                    .show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext,
                    "Please enter password!!",
                    Toast.LENGTH_LONG)
                    .show()
            return
        }

        // signin existing user
        mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Objects.requireNonNull(FirebaseAuth.getInstance().uid)?.let {
                            db!!.collection("Profile").document(it).addSnapshotListener { value, error ->
                                val cname: String
                                try {
                                    assert(value != null)
                                    cname = Objects.requireNonNull(value!!["cname"]).toString()
                                    Toast.makeText(applicationContext, "Login successful!!", Toast.LENGTH_LONG).show()
                                    // hide the progress bar
                                    progressbar!!.visibility = View.GONE
                                    // if sign-in is successful
                                    // intent to home activity
                                    val intent = Intent(this@StartActivity, HomeTActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } catch (e: Exception) {
                                    Toast.makeText(applicationContext, "Login successful!!", Toast.LENGTH_LONG).show()
                                    // hide the progress bar
                                    progressbar!!.visibility = View.GONE

                                    // if sign-in is successful
                                    // intent to home activity
                                    val intent = Intent(this@StartActivity, HomeMActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                    } else {
                        // sign-in failed
                        Toast.makeText(applicationContext,
                                "Login failed!!",
                                Toast.LENGTH_LONG)
                                .show()

                        // hide the progress bar
                        progressbar!!.visibility = View.GONE
                    }
                }
    }
}