package com.doodleblue

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView

class MyCustomAdapter(context: Context, resource: Int, private val contactsInfoList: List<*>) :  ArrayAdapter<Any?>(context, resource, contactsInfoList) {

    private inner class ViewHolder {
        var displayName: TextView? = null
        var phoneNumber: TextView? = null
        var checkbox: CheckBox? = null
        var llLinear: LinearLayout? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var holder: ViewHolder? = null
        if (convertView == null) {
            val vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = vi.inflate(R.layout.contact_info, null)
            holder = ViewHolder()
            holder.displayName = convertView!!.findViewById<View>(R.id.displayName) as TextView
            holder.phoneNumber = convertView.findViewById<View>(R.id.phoneNumber) as TextView
            holder.checkbox = convertView.findViewById<View>(R.id.checkBox) as CheckBox
            holder.llLinear = convertView.findViewById<View>(R.id.llrow) as LinearLayout
            convertView.setTag(holder)
        } else {
            holder = convertView.tag as ViewHolder
        }
        val contactsInfo: ContactsInfo =
            contactsInfoList[position] as ContactsInfo
        holder.displayName?.setText(contactsInfo.displayName)
        holder.phoneNumber?.setText(contactsInfo.phoneNumber)
        val finalHolder: ViewHolder? = holder
        holder.llLinear?.setOnClickListener(View.OnClickListener {
            if (finalHolder?.checkbox?.isChecked() == true) {
                val preferencesimg = context.getSharedPreferences(
                    "login", 0
                )
                val editorimg = preferencesimg.edit()
                editorimg.putString("mobile", contactsInfo.phoneNumber?.replace(" ", ""))
                editorimg.apply()
            } else {
                val preferencesimg = context.getSharedPreferences(
                    "login", 0
                )
                val editorimg = preferencesimg.edit()
                editorimg.putString("mobile", "")
                editorimg.apply()
            }
        })
        return convertView!!
    }
}