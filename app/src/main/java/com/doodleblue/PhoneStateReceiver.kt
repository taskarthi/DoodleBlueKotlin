package com.doodleblue

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.telephony.PhoneNumberUtils
import android.telephony.TelephonyManager
import android.widget.PopupWindow
import android.widget.Toast


class PhoneStateReceiver : BroadcastReceiver() {
    var popupWindow: PopupWindow? = null
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

                if (PhoneNumberUtils.compare(strMobile, incomingNumber)) {
                    Toast.makeText(context,"Mobile Number"+strMobile+"  Name "+strName,Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}