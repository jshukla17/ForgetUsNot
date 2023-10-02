package com.jshukla.forgetusnot

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jshukla.forgetusnot.databinding.ActivityCatchupBinding

class CatchUp : ComponentActivity() {
    private lateinit var binding: ActivityCatchupBinding
    private var contactList : List<Contact> = listOf()
    private lateinit var adapter: ContactsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel2()
        binding = ActivityCatchupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadContacts()
        binding.searchBar.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Call the search method whenever the text changes
                performSearch(s.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun performSearch(query: String) {
        // Filter the data based on the search query
        val filteredList = contactList.filter {
                contact ->
            contact.name.contains(query, true) || contact.phone.contains(query, true)
        }

        adapter = ContactsAdapter(this, filteredList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

    }

    private fun loadContacts() {
        contactList = readContacts()
        adapter = ContactsAdapter(this, contactList)
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    @SuppressLint("Range")
    private fun readContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val uniqueContacts = HashSet<String>() // HashSet to store unique contacts

        val contentResolver: ContentResolver = contentResolver
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )

        cursor?.let {
            while (cursor.moveToNext()) {
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phone =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val pictureUri =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                val contact = Contact(name, phone, pictureUri)

                // Check if the contact has already been added to the uniqueContacts set
                if (!uniqueContacts.contains(contact.name)) {
                    contacts.add(contact)
                    uniqueContacts.add(contact.name) // Add the contact name to the set
                }
            }
            cursor.close()
        }

        return contacts
    }



    private fun createNotificationChannel2() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = "Catch Up"
        val descriptionText = "To remind you to talk to people you love ♥"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("Catch Up", name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
