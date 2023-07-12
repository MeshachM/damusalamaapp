package com.example.damusalama

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.damusalama.R
import android.content.Intent
import android.os.Handler
import com.example.damusalama.StartActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val startNewMain = intent.getBooleanExtra("START_NEW_MAIN", true)
        Handler().postDelayed({ // This method will be executed once the timer is over
            if (startNewMain) {
                val i = Intent(this@MainActivity, StartActivity::class.java)
                startActivity(i)
            }
            finish()
        }, 3000)
    }
}