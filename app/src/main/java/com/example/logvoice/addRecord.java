package com.example.logvoice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class addRecord extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private MediaRecorder mediaRecorder;
    private String outputFilePath;
    private boolean isRecording = false;
    private boolean isPaused = false;
    private boolean recordingStopped = false;
    private Button saveButton;
    private EditText titleEditText;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        username = getIntent().getStringExtra("name");
        Log.d("addRecord", "Username retrieved: " + username);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        ImageButton micButton = findViewById(R.id.button_mic);
        ImageButton pauseButton = findViewById(R.id.button_pause);
        ImageButton stopButton = findViewById(R.id.button_stop);
        saveButton = findViewById(R.id.button);
        titleEditText = findViewById(R.id.editTextText);

        saveButton.setEnabled(false);
        outputFilePath = getExternalFilesDir(null).getAbsolutePath() + "/recording_" + System.currentTimeMillis() + ".mp3";

        micButton.setOnClickListener(v -> {
            if (isRecording) {
                if (isPaused) {
                    resumeRecording();
                } else {
                    pauseRecording();
                }
            } else {
                startRecording();
            }
        });

        pauseButton.setOnClickListener(v -> {
            if (isRecording) {
                if (isPaused) {
                    resumeRecording();
                } else {
                    pauseRecording();
                }
            }
        });

        stopButton.setOnClickListener(v -> {
            stopRecording();
            recordingStopped = true;
            checkIfCanSave();
        });

        saveButton.setOnClickListener(v -> {
            if (recordingStopped && !titleEditText.getText().toString().trim().isEmpty()) {
                saveRecordingToFirebase();
            } else {
                Toast.makeText(this, "Please stop the recording and enter a title.", Toast.LENGTH_SHORT).show();
            }
        });

        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkIfCanSave();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void checkIfCanSave() {
        saveButton.setEnabled(recordingStopped && !titleEditText.getText().toString().trim().isEmpty());
    }

    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(outputFilePath);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
            isPaused = false;
            recordingStopped = false;
            isRecording = true;
        } catch (IOException e) {
            Log.e("AudioRecorder", "prepare() failed: " + e.getMessage());
        }
    }

    private void pauseRecording() {
        if (mediaRecorder != null && isRecording && !isPaused) {
            mediaRecorder.pause();
            isPaused = true;
            Toast.makeText(this, "Recording paused", Toast.LENGTH_SHORT).show();
        }
    }

    private void resumeRecording() {
        if (mediaRecorder != null && isRecording && isPaused) {
            mediaRecorder.resume();
            isPaused = false;
            Toast.makeText(this, "Recording resumed", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show();
            } catch (RuntimeException stopException) {
                Log.e("AudioRecorder", "stop() failed: " + stopException.getMessage());
            } finally {
                mediaRecorder.release();
                mediaRecorder = null;
                isRecording = false;
                isPaused = false;
            }
        }
    }

    private void saveRecordingToFirebase() {
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String title = titleEditText.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("recordings");
        String recordingId = reference.push().getKey();

        HashMap<String, Object> recordingData = new HashMap<>();
        recordingData.put("username", username);
        recordingData.put("title", title);
        recordingData.put("timestamp", currentTime);
        recordingData.put("filePath", outputFilePath);

        if (recordingId != null) {
            reference.child(recordingId).setValue(recordingData).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(addRecord.this, "Recording saved successfully ", Toast.LENGTH_SHORT).show();
                    navigateToRecordingsActivity();
                } else {
                    Toast.makeText(addRecord.this, "Failed to save recording", Toast.LENGTH_SHORT).show();
                    Log.e("FirebaseError", "Failed to save recording: " + task.getException().getMessage());
                }
            }).addOnFailureListener(e -> {
                Log.e("FirebaseError", "Failed to save recording: " + e.getMessage());
                Toast.makeText(addRecord.this, "Error saving to Firebase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "Error generating recording ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToRecordingsActivity() {
        Intent intent = new Intent(addRecord.this, RecordingsActivity.class);
        intent.putExtra("name", username); // Pass the username
        startActivity(intent);
        finish(); // Optionally finish this activity if you don’t want to go back to it
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (!permissionToRecordAccepted) {
                finish(); // Close the activity if permission is not granted
            }
        }
    }
}
