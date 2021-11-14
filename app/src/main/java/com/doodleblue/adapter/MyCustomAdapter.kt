package com.doodleblue.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.doodleblue.R
import com.doodleblue.model.ContactsInfo

class MyCustomAdapter(context: Context, resource: Int, private val contactsInfoList: List<*>) :
    ArrayAdapter<Any?>(context, resource, contactsInfoList) {

    private inner class ViewHolder {
        lateinit var displayName: TextView
        lateinit var phoneNumber: TextView
        lateinit var checkbox: CheckBox
    }

    override fun getCount(): Int {
        return contactsInfoList.size
    }

    override fun getItem(position: Int): ContactsInfo {
        return contactsInfoList[position] as ContactsInfo
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var holder: ViewHolder? = null
        val result: View
        if (convertView == null) {
            val vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = vi.inflate(R.layout.contact_info, null)
            holder = ViewHolder()
            holder.displayName = convertView!!.findViewById<View>(R.id.displayName) as TextView
            holder.phoneNumber = convertView.findViewById<View>(R.id.phoneNumber) as TextView
            holder.checkbox = convertView.findViewById<View>(R.id.checkBox) as CheckBox
            result = convertView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            result = convertView
        }
        val contactsInfo: ContactsInfo =
            contactsInfoList[position] as ContactsInfo

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val preferencesimg = context.getSharedPreferences(
                    "login", 0
                )
                val editorimg = preferencesimg.edit()
                editorimg.putString("name", contactsInfo.displayName)
                editorimg.putString("mobile", contactsInfo.phoneNumber?.replace(" ", ""))
                editorimg.apply()
            } else {
                val preferencesimg = context.getSharedPreferences(
                    "login", 0
                )
                val editorimg = preferencesimg.edit()
                editorimg.putString("name", "")
                editorimg.putString("mobile", "")
                editorimg.apply()
            }
        }

        val item: ContactsInfo = getItem(position)
        holder.displayName.text = item.displayName
        holder.phoneNumber.text = item.phoneNumber
        holder.checkbox.isChecked = item.checked

        return result
    }
}