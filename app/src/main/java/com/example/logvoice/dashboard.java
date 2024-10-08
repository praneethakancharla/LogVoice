package com.example.logvoice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class dashboard extends AppCompatActivity {

    private CardView cardViewProfile, cardViewRecords, cardAddRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        // Initialize the CardView elements using their IDs
        cardViewProfile = findViewById(R.id.card_view_profile);
        cardViewRecords = findViewById(R.id.card_view_records);
        cardAddRecord = findViewById(R.id.card_add_record);
        Button logout = findViewById(R.id.button3);

        // Set OnClickListeners for each card
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboard.this, loginPage.class);
                startActivity(intent);
            }
        });
        cardViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Build the AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(dashboard.this);
                builder.setTitle("Profile Details");

                // Set the message with the user details
                String message = "Name: " + name + "\n\n" +
                        "Email: " + email + "\n\n" +
                        "Password: " + password;

                builder.setMessage(message);

                // Set the Close button
                builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

                // Create and show the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        cardViewRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle records card click
                Intent intent = new Intent(dashboard.this, RecordingsActivity.class);
                startActivity(intent);
            }
        });

        cardAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle add record card click
                Intent intent = new Intent(dashboard.this, addRecord.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Minimize the app instead of going back to the login page or any previous activity
        super.onBackPressed();
        moveTaskToBack(true);
    }
}
