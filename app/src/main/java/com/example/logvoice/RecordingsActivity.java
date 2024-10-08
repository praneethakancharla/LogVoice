package com.example.logvoice;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecordingsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecordingAdapter recordingAdapter;
    private List<Recording> recordings;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordings);

        // Retrieve the username passed from the previous activity
        username = getIntent().getStringExtra("name");
        Log.d("RecordingsActivity", "Username retrieved: " + username);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recordings = new ArrayList<>();
        recordingAdapter = new RecordingAdapter(recordings);
        recyclerView.setAdapter(recordingAdapter);

        // Fetch recordings from Firebase
        fetchRecordingsFromFirebase();
    }

    private void fetchRecordingsFromFirebase() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("recordings");

        reference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recordings.clear(); // Clear previous recordings
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Recording recording = snapshot.getValue(Recording.class);
                    if (recording != null) {
                        recordings.add(recording); // Add to the list
                    } else {
                        Log.e("RecordingsActivity", "Recording is null for snapshot: " + snapshot.getKey());
                    }
                }
                recordingAdapter.notifyDataSetChanged(); // Notify the adapter of data change
                if (recordings.isEmpty()) {
                    Toast.makeText(RecordingsActivity.this, "No recordings found for this user.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("RecordingsActivity", "Database error: " + databaseError.getMessage());
                Toast.makeText(RecordingsActivity.this, "Failed to load recordings.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}