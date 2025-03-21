package com.example.numad25sp_changliu;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the Quick Calculation button
        Button quickCalcButton = findViewById(R.id.quick_calc_button);

        // Set click listener for Quick Calculation button
        quickCalcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start QuicCalcActivity
                Intent intent = new Intent(MainActivity.this, QuicCalcActivity.class);
                startActivity(intent);
            }
        });

        // About Me button handler
        Button aboutMeButton = findViewById(R.id.about_me_button);
        aboutMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle About Me button click if needed
                // For now, this is just a placeholder
            }
        });

        // Contacts Collector button handler
        Button contactsButton = findViewById(R.id.contacts_collector_button);
        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start ContactsCollectorActivity
                Intent intent = new Intent(MainActivity.this, ContactsCollectorActivity.class);
                startActivity(intent);
            }
        });

        // Prime Finder button handler
        Button primeFinderButton = findViewById(R.id.prime_finder_button);
        primeFinderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start PrimeFinderActivity
                Intent intent = new Intent(MainActivity.this, PrimeFinderActivity.class);
                startActivity(intent);
            }
        });
    }
}