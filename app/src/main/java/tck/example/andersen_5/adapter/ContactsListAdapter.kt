package tck.example.andersen_5.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import tck.example.andersen_5.ContactRepository
import tck.example.andersen_5.R
import tck.example.andersen_5.classes.Contact
import tck.example.andersen_5.fragments.ContactListFragment
import java.util.*


class ContactsListAdapter(context: ContactListFragment, var contact:List<Contact>): RecyclerView.Adapter<ContactHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return ContactHolder(view)
    }
    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        val contact = contact[position]
        holder.bind(contact)
    }
    override fun getItemCount(): Int {
        return contact.size
    }
}

class ContactHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener,View.OnLongClickListener {
    private lateinit var contact: Contact
    private val firstNameTextView: TextView = itemView.findViewById(R.id.contactFirstname)
    private val secondNameTextView: TextView = itemView.findViewById(R.id.contactSecondName)
    private val phoneNumberTextView: TextView = itemView.findViewById(R.id.contactPhoneNumber)
    init {
        itemView.setOnClickListener(this)
    }
    fun bind(contact: Contact){
        this.contact = contact
        firstNameTextView.text = this.contact.firstName
        secondNameTextView.text = this.contact.secondName
        phoneNumberTextView.text = this.contact.phoneNumber
    }

    override fun onClick(view: View?) {
        ContactListFragment.callbacks?.onContactSelected(contact.id)
    }

    override fun onLongClick(p0: View?): Boolean {
        val builder = AlertDialog.Builder(p0!!.context)
        builder.setTitle("Delete contact")
        builder.setCancelable(false)
        builder.setMessage("Delete this contact?")
        builder.setPositiveButton("yes") { _, _ ->
            try {

                } catch (e: Exception) {
                Log.d("TAG", "deleteContact error")
            }
        }
        builder.setNegativeButton("No", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()

        return true
    }
}
