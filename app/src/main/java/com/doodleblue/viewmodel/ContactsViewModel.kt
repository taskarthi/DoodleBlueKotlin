package com.doodleblue.viewmodel

import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doodleblue.model.ContactsInfo
import java.util.ArrayList

class ContactsViewModel: ViewModel() {

    var contactsInfoLiveData: MutableLiveData<ArrayList<ContactsInfo>>? = null

    fun getContacts(cr: ContentResolver) {
        val contactsInfoList = ArrayList<ContactsInfo>()
        val cur: Cursor? =
            cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        if (cur!!.getCount() > 0) {
            while (cur?.moveToNext()) {
                val contactsInfo = ContactsInfo()
                var name: String =
                    cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                var phonenumber: String =
                    cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                name = name.replace(",".toRegex(), "")
                phonenumber = phonenumber.replace(",".toRegex(), "")
                contactsInfo.displayName = name
                contactsInfo.phoneNumber = phonenumber
                contactsInfoList!!.add(contactsInfo)
            }
        }
        contactsInfoLiveData?.postValue(contactsInfoList)
    }


}