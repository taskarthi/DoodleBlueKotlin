package com.doodleblue

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.doodleblue.adapter.MyCustomAdapter
import com.doodleblue.broadcast.PhoneStateReceiver
import com.doodleblue.databinding.ActivityReadcontactBinding
import com.doodleblue.model.ContactsInfo
import com.doodleblue.viewmodel.ContactsViewModel
import kotlinx.android.synthetic.main.activity_readcontact.*
import java.util.*
import kotlin.collections.ArrayList

class ReadContactActivity : AppCompatActivity() {

    private var dataAdapter: MyCustomAdapter? = null
    private var phReceiver: PhoneStateReceiver? = null
    private var binding: ActivityReadcontactBinding? = null
    private lateinit var contactsViewModel: ContactsViewModel
    private var contacts = ArrayList<ContactsInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        contactsViewModel =
            ViewModelProvider(this).get(ContactsViewModel::class.java)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_readcontact)
        contactsViewModel.getContacts(contentResolver)
        lstContacts!!.adapter = dataAdapter
        phReceiver = PhoneStateReceiver()
        requestContactPermission()
        requestCalls()
        requestCallsLog()

        contactsViewModel.contactsInfoLiveData?.observe(this, androidx.lifecycle.Observer {
            try {
                contacts = it
                dataAdapter = MyCustomAdapter(
                    applicationContext, R.layout.contact_info,
                    it!!
                )
                lstContacts!!.adapter = dataAdapter
                lstContacts!!.setOnItemClickListener { parent, view, position, id ->
                    val dataModel: ContactsInfo = it!![position] as ContactsInfo
                    dataModel.checked = !dataModel.checked
                    dataAdapter!!.notifyDataSetChanged()
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        })
    }

    private fun requestCalls() {
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

    private fun requestCallsLog() {
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

    private fun requestContactPermission() {
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
