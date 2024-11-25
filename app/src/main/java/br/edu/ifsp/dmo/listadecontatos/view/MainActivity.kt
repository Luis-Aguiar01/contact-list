package br.edu.ifsp.dmo.listadecontatos.view

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.dmo.listadecontatos.R
import br.edu.ifsp.dmo.listadecontatos.databinding.ActivityMainBinding
import br.edu.ifsp.dmo.listadecontatos.databinding.NewContactDialogBinding
import br.edu.ifsp.dmo.listadecontatos.model.Contact
import br.edu.ifsp.dmo.listadecontatos.model.ContactDao

class MainActivity : AppCompatActivity(), OnItemClickListener {

    private val TAG = "CONTACTS"
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingDialog: NewContactDialogBinding
    private lateinit var adapter: ListContactAdapter
    private var isDialogOpen = false
    private val listDatasource = ArrayList<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            val name = savedInstanceState.getString("name")
            val phone = savedInstanceState.getString("phone");
            isDialogOpen = savedInstanceState.getBoolean("isDialogOpen")

            if (isDialogOpen) {
                handleNewContactDialog(name, phone)
            }
        }
        Log.v(TAG, "Executando o onCreate()")
        configClickListener()
        configListView()
    }

    override fun onStart() {
        Log.v(TAG, "Executando o onStart()")
        super.onStart()
    }

    override fun onResume() {
        Log.v(TAG, "Executando o onResume()")
        super.onResume()
    }

    override fun onPause() {
        Log.v(TAG, "Executando o onPause()")
        super.onPause()
    }

    override fun onStop() {
        Log.v(TAG, "Executando o onStop()")
        super.onStop()
    }

    override fun onRestart() {
        Log.v(TAG, "Executando o onRestart()")
        super.onRestart()
    }

    override fun onDestroy() {
        Log.v(TAG, "Executando o onDestroy()")
        Log.v(TAG, "Lista de contatos que será perdida")
        for (contact in ContactDao.findAll()) {
            Log.v(TAG, contact.toString())
        }
        super.onDestroy()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectContact = binding.listViewContacts.adapter.getItem(position) as Contact
        val uri = "tel:${selectContact.phone}"
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse(uri)
        startActivity(intent)
    }

     private fun configClickListener() {
        binding.buttonNewContact.setOnClickListener {
            handleNewContactDialog()
        }
    }

    private fun configListView() {
        listDatasource.addAll(ContactDao.findAll())
        adapter = ListContactAdapter(this, listDatasource)
        binding.listViewContacts.adapter = adapter
        binding.listViewContacts.onItemClickListener = this
    }

    private fun updateListDatasource() {
        listDatasource.clear()
        listDatasource.addAll(ContactDao.findAll().sortedBy(Contact::name))
        adapter.notifyDataSetChanged()
    }

    private fun handleNewContactDialog(name: String? = "", phone: String? = "") {
        bindingDialog = NewContactDialogBinding.inflate(layoutInflater)
        bindingDialog.editTextPhone.setText(phone)
        bindingDialog.editTextName.setText(name)

        val builderDialog = AlertDialog.Builder(this)
        builderDialog.setView(bindingDialog.root)
            .setTitle(R.string.new_contact)
            .setPositiveButton(
                R.string.btn_dialog_save,
                DialogInterface.OnClickListener { dialog, which ->
                    Log.v(TAG, getString(R.string.btn_dialog_save))
                    ContactDao.insert(
                        Contact(
                            bindingDialog.editTextName.text.toString(),
                            bindingDialog.editTextPhone.text.toString()
                        )
                    )
                    updateListDatasource()
                    isDialogOpen = false
                    dialog.dismiss()
                })
            .setNegativeButton(
                R.string.btn_dialog_cancel,
                DialogInterface.OnClickListener { dialog, which ->
                    Log.v(TAG, getString(R.string.btn_dialog_cancel))
                    isDialogOpen = false
                    dialog.cancel()
                })
            .setOnDismissListener {
                isDialogOpen = false
            }

        builderDialog.create().show()
        isDialogOpen = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (isDialogOpen) {
            val name = bindingDialog.editTextName.text.toString()
            val phone =  bindingDialog.editTextPhone.text.toString()
            outState.putString("name", name)
            outState.putString("phone", phone)
        }
        outState.putBoolean("isDialogOpen", isDialogOpen)
    }
}