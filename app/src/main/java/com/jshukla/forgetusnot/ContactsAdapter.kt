package com.jshukla.forgetusnot

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView


class ContactsAdapter(private val context: Context, private val contacts: List<Contact>) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contacts_view, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bind(contact)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, LovedOneActivity::class.java )
            intent.putExtra("uri", contact.pictureUri)
            intent.putExtra("name", contact.name)
            startActivity(context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }
    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar:ImageView = itemView.findViewById(R.id.contact_avatar)
        val contact_name: TextView = itemView.findViewById<TextView>(R.id.contact_name)
        val  contact_phone= itemView.findViewById<TextView>(R.id.contact_phone)
        fun bind(contact: Contact) {
            contact_name.text = contact.name
            contact_phone.text = contact.phone

            contact.pictureUri?.let {
                val photoUri = Uri.parse(contact.pictureUri)
                avatar.setImageURI(photoUri)
            } ?: run {
                avatar.setImageResource(R.mipmap.ic_launcher_foreground)
            }
        }
    }
}