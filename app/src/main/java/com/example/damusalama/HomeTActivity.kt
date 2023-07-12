package com.example.damusalama

import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.os.Bundle
import com.example.damusalama.R
import androidx.drawerlayout.widget.DrawerLayout
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import com.example.damusalama.StartActivity
import com.example.damusalama.HomeTechMakeActivity
import com.example.damusalama.HomeTechViewActivity
import com.example.damusalama.HomeTechAnswerActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import java.util.*

class HomeTActivity : AppCompatActivity() {
    var picker: DatePickerDialog? = null
    var date: EditText? = null
    var spinner: Spinner? = null
    var subtn: Button? = null
    var tech: Button? = null
    var dn: String? = null
    var num: String? = null
    var id: String? = null
    var dad: String? = null
    var sp: String? = null
    var dt: String? = null
    var dname: EditText? = null
    var idno: EditText? = null
    var dadd: EditText? = null
    var number: EditText? = null
    private var mAuth: FirebaseAuth? = null
    private var db: FirebaseFirestore? = null
    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_t)
        toolbar = findViewById<View>(R.id.my_toolbar_t) as Toolbar
        setSupportActionBar(toolbar)
        val drawer = findViewById<DrawerLayout>(R.id.drawerT)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()
        subtn = findViewById(R.id.subtn)
        dname = findViewById(R.id.dname)
        idno = findViewById(R.id.idno)
        dadd = findViewById(R.id.dadd)
        number = findViewById(R.id.number)
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
        date = findViewById(R.id.date)
        date?.setInputType(InputType.TYPE_NULL)
        date?.setOnClickListener(View.OnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr[Calendar.DAY_OF_MONTH]
            val month = cldr[Calendar.MONTH]
            val year = cldr[Calendar.YEAR]
            // date picker dialog
            picker = DatePickerDialog(this@HomeTActivity,
                    { view, year, monthOfYear, dayOfMonth -> date?.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year) }, year, month, day)
            picker!!.show()
        })
        subtn?.setOnClickListener(View.OnClickListener { sendtodb() })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sign_out -> {
                val intent = Intent(this@HomeTActivity, StartActivity::class.java)
                intent.putExtra("action", "logout")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //makesure user cant go back
                startActivity(intent)
            }
            R.id.nav_make -> {
                val intent2 = Intent(this@HomeTActivity, HomeTechMakeActivity::class.java)
                startActivity(intent2)
            }
            R.id.nav_viewreq -> {
                val intent3 = Intent(this@HomeTActivity, HomeTechViewActivity::class.java)
                startActivity(intent3)
            }
            R.id.nav_answer -> {
                val intent4 = Intent(this@HomeTActivity, HomeTechAnswerActivity::class.java)
                startActivity(intent4)
            }
        }
        return true
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
        dn = dname!!.text.toString()
        id = idno!!.text.toString()
        num = number!!.text.toString()
        dad = dadd!!.text.toString()
        dt = date!!.text.toString()
        sp = spinner!!.selectedItem.toString()
        val details = HashMap<String, String>()
        details["dname"] = dn!!
        details["idno"] = id!!
        details["dadd"] = dad!!
        details["number"] = num!!
        details["date"] = dt!!
        details["spinner"] = sp!!
        db!!.collection("DonorsInfo").add(details).addOnCompleteListener(object : OnCompleteListener<Any?> {
            override fun onComplete(p0: Task<Any?>) {
                dname!!.setText("")
                idno!!.setText("")
                number!!.setText("")
                dadd!!.setText("")
                date!!.setText("")
                //                                Intent intent = new Intent(HomeTActivity.this, HomeMActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
            }
        })
        //                    }
//                }
//            }
//        });
    } //    @Override
    //    public boolean onCreateOptionsMenu(Menu menu) {
    //        getMenuInflater().inflate(R.menu.main_menu, menu);
    //        return true;
    //    }
    //    @Override
    //    public boolean onOptionsItemSelected(MenuItem item) {
    //        if(item.getItemId() == R.id.menu_sign_out) {
    //            mAuth.getInstance().signOut(this)
    //                    .addOnCompleteListener(new OnCompleteListener<Void>() {
    //                        @Override
    //                        public void onComplete(@NonNull Task<Void> task) {
    //                            Toast.makeText(HomeTActivity.this,
    //                                    "You have been signed out.",
    //                                    Toast.LENGTH_LONG)
    //                                    .show();
    //                            // Close activity
    //                            finish();
    //                        }
    //                    });
    //        }
    //        return true;
    //    }
    //            try {
    //                Point destinationPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());
    //                assert locationComponent.getLastKnownLocation() != null;
    //                Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
    //                        locationComponent.getLastKnownLocation().getLatitude());
    //            }catch(Exception ex){
    //                Toast.makeText(getApplicationContext(),"Null Error",Toast.LENGTH_LONG).show();
    //            }
}