package tck.example.andersen_5.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tck.example.andersen_5.classes.Contact
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import tck.example.andersen_5.R
import tck.example.andersen_5.dialogs.deleteContactDialog
import tck.example.andersen_5.fragments.ContactListFragment

class ContactsListAdapter(var contact:MutableList<Contact>): RecyclerView.Adapter<ContactHolder>(),Filterable{

    private var contactListFull: MutableList<Contact> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return ContactHolder(view)
    }
    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        val contact = contact[position]
        holder.bind(contact)

        holder.view.setOnLongClickListener{
            deleteContactDialog(holder.view.context,contact.id)
            true
        }
    }
    override fun getItemCount(): Int {
        return contact.size
    }
    override fun getFilter(): Filter {
        return exampleFilter
    }

    private val exampleFilter: Filter = object : Filter() {

        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<Contact> = mutableListOf()
            if (constraint.isEmpty()) {
                filteredList.addAll(contactListFull)
            } else {
                val filter = constraint.toString().lowercase().trim { it <= ' ' }
                for (item in contactListFull) {
                    when{
                        item.firstName.lowercase().contains(filter) -> filteredList.add(item)
                        item.secondName.lowercase().contains(filter) -> filteredList.add(item)
                        item.phoneNumber.lowercase().contains(filter) -> filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            contact.clear()
            contact.addAll(results.values as List<Contact>)
            notifyDataSetChanged()
        }
    }
    init {
        contactListFull.addAll(contact)
    }
}

class ContactHolder(val view: View): RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener{
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

    override fun onLongClick(view: View?): Boolean {
        return true
    }
}

class MyDiffUtil(private val oldList:List<Contact>,private val newList:List<Contact>): DiffUtil.Callback(){
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].id != newList[newItemPosition].id -> false
            oldList[oldItemPosition].firstName != newList[newItemPosition].firstName -> false
            oldList[oldItemPosition].secondName != newList[newItemPosition].secondName -> false
            else -> true
        }
    }
}