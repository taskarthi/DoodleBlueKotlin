package com.doodleblue

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class IncomingCallActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            window.addFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            )
            setContentView(R.layout.popup)
            super.onCreate(savedInstanceState)
            val strMobile = intent.extras!!.getString("mobile")
            val txtName = findViewById<View>(R.id.nametxt) as TextView
            val txtMobile = findViewById<View>(R.id.mobiletxt) as TextView
            txtMobile.text = "Mobile Number :$strMobile"
        } catch (e: Exception) {
            Log.d("Exception", e.toString())
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }
}