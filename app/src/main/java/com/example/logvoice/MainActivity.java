package com.example.logvoice;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Correct initialization of the button
        button = findViewById(R.id.button); // Ensure this ID matches with your layout XML

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, loginPage.class); // Ensure the class name is capitalized
                startActivity(intent); // Start the second activity
            }
        });
    }
}
