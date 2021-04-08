package com.doodleblue

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*


class ReadContactActivity : AppCompatActivity() {
    var dataAdapter: MyCustomAdapter? = null
    var listView: ListView? = null
    var contactsInfoList: MutableList<ContactsInfo?>? = null
    var phReceiver: PhoneStateReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_readcontact)
        listView = findViewById<View>(R.id.lstContacts) as ListView
        listView!!.adapter = dataAdapter
        phReceiver = PhoneStateReceiver()
        requestContactPermission()
        requestcalls()
        requestcallsLog()
    }

    private val contacts: Unit
        private get (){
            contactsInfoList = ArrayList<ContactsInfo?>()
            val cr = contentResolver
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
            try {
                dataAdapter = MyCustomAdapter(
                    applicationContext, R.layout.contact_info,
                    contactsInfoList!!
                )
                listView!!.adapter = dataAdapter
                listView!!.setOnItemClickListener { parent, view, position, id ->
                    val dataModel: ContactsInfo = contactsInfoList!![position] as ContactsInfo
                    dataModel.checked = !dataModel.checked
                    dataAdapter!!.notifyDataSetChanged()
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }

    fun requestcalls() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                val permissions =
                    arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE)
                requestPermissions(permissions, PERMISSION_REQUEST_READ_PHONE_STATE)
            }
        }
    }

    fun requestcallsLog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this@ReadContactActivity,
                    Manifest.permission.READ_CALL_LOG
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this@ReadContactActivity,
                    Manifest.permission.SYSTEM_ALERT_WINDOW
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@ReadContactActivity,
                    arrayOf(
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.SYSTEM_ALERT_WINDOW
                    ),
                    PERMISSION_REQUEST_READ_CALLLOG
                )
            }
        }
    }

    fun requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_CONTACTS
                    )
                ) {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Read contacts access needed")
                    builder.setPositiveButton(android.R.string.ok, null)
                    builder.setMessage("Please enable access to contacts.")
                    builder.setOnDismissListener {
                        requestPermissions(
                            arrayOf(Manifest.permission.READ_CONTACTS),
                            PERMISSIONS_REQUEST_READ_CONTACTS
                        )
                    }
                    builder.show()
                } else {
                    ActivityCompat.requestPermissions(
                        this, arrayOf(Manifest.permission.READ_CONTACTS),
                        PERMISSIONS_REQUEST_READ_CONTACTS
                    )
                }
            } else {
                contacts
            }
        } else {
            contacts
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    contacts
                } else {
                    Toast.makeText(
                        this,
                        "You have disabled a contacts permission",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
            PERMISSION_REQUEST_READ_PHONE_STATE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    finish()
                }
                return
            }
            PERMISSION_REQUEST_READ_CALLLOG -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return
            }
        }
    }

    companion object {
        const val PERMISSIONS_REQUEST_READ_CONTACTS = 1
        private const val PERMISSION_REQUEST_READ_PHONE_STATE = 2
        private const val PERMISSION_REQUEST_READ_CALLLOG = 3
    }
}
