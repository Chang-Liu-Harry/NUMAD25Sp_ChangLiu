package com.example.numad25sp_changliu;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;

public class ContactsCollectorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private ArrayList<Contact> contacts;
    private ConstraintLayout constraintLayout;

    // Key for saving instance state
    private static final String KEY_CONTACTS = "contacts_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_collector);

        // Enable back button in action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contacts Collector");

        // Initialize the constraint layout
        constraintLayout = findViewById(R.id.contacts_constraint_layout);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.contacts_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize contacts list
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_CONTACTS)) {
            // Restore contacts from saved state
            contacts = savedInstanceState.getParcelableArrayList(KEY_CONTACTS);
        } else {
            // Create new contacts list with sample data
            contacts = new ArrayList<>();
            // Add some sample contacts for demonstration
            contacts.add(new Contact("John Doe", "123-456-7890"));
            contacts.add(new Contact("Jane Smith", "234-567-8901"));
            contacts.add(new Contact("Alice Johnson", "345-678-9012"));
        }

        // Initialize adapter
        adapter = new ContactAdapter(contacts, new ContactAdapter.OnContactClickListener() {
            @Override
            public void onContactClick(Contact contact) {
                // Open dialer with the contact's phone number
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contact.getPhoneNumber()));
                startActivity(intent);
            }

            @Override
            public void onEditClick(int position) {
                showEditContactDialog(position);
            }

            @Override
            public void onDeleteClick(int position) {
                deleteContact(position);
            }
        });

        recyclerView.setAdapter(adapter);

        // Set up Floating Action Button
        FloatingActionButton fab = findViewById(R.id.fab_add_contact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddContactDialog();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save contacts list to bundle
        outState.putParcelableArrayList(KEY_CONTACTS, contacts);
    }

    private void showAddContactDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_contact, null);

        final EditText nameInput = dialogView.findViewById(R.id.edit_contact_name);
        final EditText phoneInput = dialogView.findViewById(R.id.edit_contact_phone);

        builder.setView(dialogView)
                .setTitle("Add New Contact")
                .setPositiveButton("Add", (dialog, id) -> {
                    String name = nameInput.getText().toString().trim();
                    String phone = phoneInput.getText().toString().trim();

                    if (!name.isEmpty() && !phone.isEmpty()) {
                        addContact(name, phone);
                    } else {
                        Snackbar.make(constraintLayout, "Name and phone number are required", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        builder.create().show();
    }

    private void showEditContactDialog(final int position) {
        Contact contact = contacts.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_contact, null);

        final EditText nameInput = dialogView.findViewById(R.id.edit_contact_name);
        final EditText phoneInput = dialogView.findViewById(R.id.edit_contact_phone);

        // Pre-fill with existing contact information
        nameInput.setText(contact.getName());
        phoneInput.setText(contact.getPhoneNumber());

        builder.setView(dialogView)
                .setTitle("Edit Contact")
                .setPositiveButton("Save", (dialog, id) -> {
                    String name = nameInput.getText().toString().trim();
                    String phone = phoneInput.getText().toString().trim();

                    if (!name.isEmpty() && !phone.isEmpty()) {
                        contacts.get(position).setName(name);
                        contacts.get(position).setPhoneNumber(phone);
                        adapter.notifyItemChanged(position);

                        Snackbar.make(constraintLayout, "Contact updated", Snackbar.LENGTH_SHORT)
                                .setAction("UNDO", v -> {
                                    contacts.get(position).setName(contact.getName());
                                    contacts.get(position).setPhoneNumber(contact.getPhoneNumber());
                                    adapter.notifyItemChanged(position);
                                })
                                .show();
                    } else {
                        Snackbar.make(constraintLayout, "Name and phone number are required", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        builder.create().show();
    }

    private void addContact(String name, String phone) {
        Contact newContact = new Contact(name, phone);
        contacts.add(newContact);
        adapter.notifyItemInserted(contacts.size() - 1);

        Snackbar.make(constraintLayout, "Contact added", Snackbar.LENGTH_SHORT)
                .setAction("UNDO", v -> {
                    contacts.remove(contacts.size() - 1);
                    adapter.notifyItemRemoved(contacts.size());
                })
                .show();
    }

    private void deleteContact(final int position) {
        final Contact deletedContact = contacts.get(position);
        contacts.remove(position);
        adapter.notifyItemRemoved(position);

        Snackbar.make(constraintLayout, "Contact deleted", Snackbar.LENGTH_SHORT)
                .setAction("UNDO", v -> {
                    contacts.add(position, deletedContact);
                    adapter.notifyItemInserted(position);
                })
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // This will navigate back to the previous activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}