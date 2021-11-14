package com.doodleblue.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.telephony.PhoneNumberUtils
import android.telephony.TelephonyManager
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.doodleblue.R


class PhoneStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        println("Receiver start")
        val state = intent.extras!!.getString(TelephonyManager.EXTRA_STATE)
        var incomingNumber = intent.extras!!.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
        if (state == TelephonyManager.EXTRA_STATE_RINGING) {
            val preferences = context.getSharedPreferences("login", 0)
            val strMobile = preferences.getString("mobile", null)
            val strName = preferences.getString("name", null)
            Log.i("mobile", strMobile.toString())
            Log.i("mobile1", incomingNumber.toString())

            if (!strMobile.isNullOrEmpty() && PhoneNumberUtils.compare(
                    context,
                    strMobile,
                    incomingNumber
                )
            ) {
                //start activity which has dialog
                val toast: Toast = Toast.makeText(
                    context,
                    "Name: $strName Number: $incomingNumber",
                    Toast.LENGTH_LONG
                )
                val toastView = toast.view // This'll return the default View of the Toast.
                val toastMessage = toastView!!.findViewById<View>(android.R.id.message) as TextView
                toastMessage.textSize = 25f
                toastMessage.setTextColor(Color.WHITE)
                toastMessage.setCompoundDrawablesWithIntrinsicBounds(
                    R.mipmap.ic_launcher_round,
                    0,
                    0,
                    0
                )
                toastMessage.gravity = Gravity.CENTER
                toastMessage.compoundDrawablePadding = 16
                toastView.setBackgroundColor(Color.BLACK)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }

        }
    }
}