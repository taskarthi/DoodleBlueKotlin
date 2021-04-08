package com.doodleblue

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneNumberUtils
import android.telephony.TelephonyManager
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog


class PhoneStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        try {
            println("Receiver start")
            val state = intent.extras!!.getString(TelephonyManager.EXTRA_STATE)
            var incomingNumber = intent.extras!!.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                incomingNumber = incomingNumber!!.substring(3)
                val preferences = context.getSharedPreferences("login", 0)
                val strMobile = preferences.getString("mobile", null)
                val strName   = preferences.getString("name", null)
                Log.i("mobile",strMobile.toString());
                if(PhoneNumberUtils.compare(strMobile, incomingNumber)) {
                    /*val dlgBuilder = AlertDialog.Builder(context)
                    dlgBuilder.setTitle("Context Example")
                    dlgBuilder.setMessage("I am being shown from the application Static context!")
                    dlgBuilder.setNeutralButton("Ok", null)
                    dlgBuilder.show()*/
                    for (i in 0..8) {

                        Toast.makeText(
                            context,
                            "Mobile Number " + strMobile + "  Name " + strName,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}