package com.doodleblue

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneNumberUtils
import android.telephony.TelephonyManager
import android.widget.PopupWindow


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
                if (PhoneNumberUtils.compare(strMobile, incomingNumber)) {
                    val i = Intent(context, IncomingCallActivity::class.java)
                    i.putExtra("mobile", strMobile)
                    i.putExtras(intent)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    context.startActivity(i)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}