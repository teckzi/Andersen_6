package tck.example.andersen_5.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tck.example.andersen_5.classes.Contact
import tck.example.andersen_5.R
import tck.example.andersen_5.adapter.ContactsListAdapter
import tck.example.andersen_5.viewModel.ContactListViewModel
import java.util.*
import android.view.inputmethod.EditorInfo




private const val TAG = "ContactListFragment"
class ContactListFragment: Fragment() {
    interface Callbacks{
        fun onContactSelected(contactId: UUID)
    }

    private lateinit var contactRecyclerView: RecyclerView
    private val contactsList = mutableListOf<Contact>()
    private var adapter: ContactsListAdapter? = ContactsListAdapter(contactsList)
    private val contactListViewModel: ContactListViewModel by lazy {
        ViewModelProvider(this).get(ContactListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_list, container, false)
        contactRecyclerView = view.findViewById(R.id.contacts_recycler_view)
        contactRecyclerView.layoutManager = LinearLayoutManager(context)
        contactRecyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactListViewModel.contactListLiveData.observe(viewLifecycleOwner, { contacts ->
                contacts.let {
                    Log.d(TAG, "Got contacts ${contacts.size}")
                    updateUI(contacts)} })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun updateUI(contacts: List<Contact>){
        if (contacts.isEmpty()) {}
        adapter = ContactsListAdapter(contacts.toMutableList())
        contactRecyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.contact_list_menu,menu)

        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter?.filter?.filter(newText)
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.new_contact -> {
                val contact = Contact()
                contactListViewModel.addContact(contact)
                callbacks?.onContactSelected(contact.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object{
        fun newInstance(): ContactListFragment {
            return ContactListFragment()
        }
        var callbacks:Callbacks? = null
    }
}