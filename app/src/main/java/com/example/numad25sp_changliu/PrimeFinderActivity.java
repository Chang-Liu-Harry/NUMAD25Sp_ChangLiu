package com.example.numad25sp_changliu;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class PrimeFinderActivity extends AppCompatActivity {

    private Button findPrimesButton;
    private Button terminateSearchButton;
    private CheckBox pacifierSwitch;
    private TextView currentNumberTextView;
    private TextView latestPrimeTextView;

    private volatile boolean isSearching = false;
    private volatile int currentNumber = 3;
    private volatile int latestPrime = 2;
    private Thread searchThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prime_finder);

        // Initialize UI components
        findPrimesButton = findViewById(R.id.find_primes_button);
        terminateSearchButton = findViewById(R.id.terminate_search_button);
        pacifierSwitch = findViewById(R.id.pacifier_switch);
        currentNumberTextView = findViewById(R.id.current_number_text);
        latestPrimeTextView = findViewById(R.id.latest_prime_text);

        // Set initial values
        currentNumberTextView.setText("Current number: Not checking");
        latestPrimeTextView.setText("Latest prime: None");

        // Configure Find Primes button
        findPrimesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSearching) {
                    startPrimeSearch();
                } else {
                    // If search is already running and button is pressed again,
                    // restart from beginning
                    stopPrimeSearch();
                    startPrimeSearch();
                }
            }
        });

        // Configure Terminate Search button
        terminateSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPrimeSearch();
            }
        });

        // No functionality needed for pacifier switch - just exists for checking/unchecking
    }

    private void startPrimeSearch() {
        isSearching = true;
        currentNumber = 3; // Start from 3

        searchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isSearching) {
                    final int numberToCheck = currentNumber;

                    // Update the UI to show the current number being checked
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            currentNumberTextView.setText("Current number: " + numberToCheck);
                        }
                    });

                    // Check if the current number is prime
                    if (isPrime(numberToCheck)) {
                        final int prime = numberToCheck;
                        latestPrime = prime;

                        // Update the UI to show the latest prime found
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                latestPrimeTextView.setText("Latest prime: " + prime);
                            }
                        });
                    }

                    // Increment by 2 (since even numbers except 2 aren't prime)
                    currentNumber += 2;
                }
            }
        });

        searchThread.start();
    }

    private void stopPrimeSearch() {
        isSearching = false;
        if (searchThread != null) {
            searchThread = null;
        }
    }

    private boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }
        if (num <= 3) {
            return true;
        }
        if (num % 2 == 0 || num % 3 == 0) {
            return false;
        }

        int i = 5;
        while (i * i <= num) {
            if (num % i == 0 || num % (i + 2) == 0) {
                return false;
            }
            i += 6;
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the state of the search, current number, and latest prime
        outState.putBoolean("isSearching", isSearching);
        outState.putInt("currentNumber", currentNumber);
        outState.putInt("latestPrime", latestPrime);
        outState.putBoolean("pacifierState", pacifierSwitch.isChecked());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore the state of the search, current number, and latest prime
        if (savedInstanceState != null) {
            isSearching = savedInstanceState.getBoolean("isSearching", false);
            currentNumber = savedInstanceState.getInt("currentNumber", 3);
            latestPrime = savedInstanceState.getInt("latestPrime", 2);
            pacifierSwitch.setChecked(savedInstanceState.getBoolean("pacifierState", false));

            // Restore the display of current number and latest prime
            if (isSearching) {
                currentNumberTextView.setText("Current number: " + currentNumber);
                latestPrimeTextView.setText("Latest prime: " + latestPrime);
                startPrimeSearch();
            }
        }
    }

    @Override
    protected void onDestroy() {
        stopPrimeSearch();
        super.onDestroy();
    }
}