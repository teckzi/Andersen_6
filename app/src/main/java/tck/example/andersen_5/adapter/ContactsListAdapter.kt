package tck.example.andersen_5.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tck.example.andersen_5.contactsDatabase.Contact
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import coil.load
import tck.example.andersen_5.R
import tck.example.andersen_5.dialogs.deleteContactDialog
import tck.example.andersen_5.fragments.ContactListFragment

class ContactsListAdapter(var contactsListMain:List<Contact>): RecyclerView.Adapter<ContactHolder>(),Filterable{
    private var contactListSecond: MutableList<Contact> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return ContactHolder(view)
    }
    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        val contact = contactsListMain[position]
        holder.bind(contact)

        holder.view.setOnLongClickListener{
            deleteContactDialog(holder.view.context,contact.id)
            true
        }
    }
    override fun getItemCount(): Int = contactsListMain.size
    override fun getFilter(): Filter = exampleFilter

    private val exampleFilter: Filter = object : Filter() {

        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<Contact> = mutableListOf()
            if (constraint.isEmpty()) filteredList.addAll(contactListSecond)
            else {
                val filter = constraint.toString().lowercase().trim { it <= ' ' }
                contactListSecond.forEach {
                    if (it.firstName.lowercase().contains(filter)){filteredList.add(it)}
                    else if(it.secondName.lowercase().contains(filter)) {filteredList.add(it)}
                }
            }
            return FilterResults().apply {
                values = filteredList
            }
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            setData(results.values as List<Contact>)
        }
    }

    fun setData(newList: List<Contact>){
        val diffUtil = MyDiffUtil(contactsListMain,newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        contactsListMain = newList
        diffResult.dispatchUpdatesTo(this)
    }

    init {
        contactListSecond.addAll(contactsListMain)
    }
}

class ContactHolder(val view: View): RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener{
    private lateinit var contact: Contact
    private val firstNameTextView: TextView = itemView.findViewById(R.id.contactFirstname)
    private val secondNameTextView: TextView = itemView.findViewById(R.id.contactSecondName)
    private val phoneNumberTextView: TextView = itemView.findViewById(R.id.contactPhoneNumber)
    private val photoImageView:ImageView = itemView.findViewById(R.id.photoImageView)
    init {
        itemView.setOnClickListener(this)
    }
    fun bind(contact: Contact){
        this.contact = contact
        firstNameTextView.text = this.contact.firstName
        secondNameTextView.text = this.contact.secondName
        phoneNumberTextView.text = this.contact.phoneNumber
        photoImageView.load(this.contact.photoUrl)
    }

    override fun onClick(view: View?) {
        ContactListFragment.callbacks?.onContactSelected(contact.id)
    }

    override fun onLongClick(view: View?): Boolean {
        return true
    }
}

class MyDiffUtil(private val oldList:List<Contact>, private val newList:List<Contact>): DiffUtil.Callback(){
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
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}