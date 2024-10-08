package com.example.logvoice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class loginPage extends AppCompatActivity {
    EditText username; // This will still capture the input from the user
    EditText password;
    Button loginButton;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        btn = findViewById(R.id.button2);

        // Navigate to the sign-up page when 'btn' is clicked
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loginPage.this, signUpPage.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for the loginButton
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateUsername() & validatePassword()) {
                    checkUser();
                }
            }
        });
    }

    public Boolean validateUsername() {
        String val = username.getText().toString();
        if (val.isEmpty()) {
            username.setError("Name cannot be empty"); // Updated message
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = password.getText().toString();
        if (val.isEmpty()) {
            password.setError("Password cannot be empty");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String userName = username.getText().toString().trim(); // Now this represents 'name'
        String userPassword = password.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("name").equalTo(userName); // Changed to 'name'

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    username.setError(null);

                    // Retrieve password from DB
                    String passwordFromDB = snapshot.child(userName).child("password").getValue(String.class);

                    if (passwordFromDB != null && passwordFromDB.equals(userPassword)) {
                        username.setError(null);

                        // Retrieve other user data
                        String emailFromDB = snapshot.child(userName).child("email").getValue(String.class);
                        String nameFromDB = snapshot.child(userName).child("name").getValue(String.class);

                        // Navigate to the main activity
                        Intent intent = new Intent(loginPage.this, dashboard.class);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("name", nameFromDB); // Sending 'name'
                        intent.putExtra("password", passwordFromDB);
                        startActivity(intent);
                    } else {
                        password.setError("Invalid Credentials");
                        password.requestFocus();
                    }
                } else {
                    username.setError("User does not exist");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Show error message
                Toast.makeText(loginPage.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

