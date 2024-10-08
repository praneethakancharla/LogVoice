package com.example.logvoice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class DisplayActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private String filePath;
    private String title;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    private static final String TAG = "DisplayActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display); // Ensure the correct layout is set

        // Retrieve data from the intent
        filePath = getIntent().getStringExtra("filePath");
        title = getIntent().getStringExtra("title");

        // Set up UI elements
        TextView titleTextView = findViewById(R.id.recording_title);
        ImageButton playButton = findViewById(R.id.play);
        TextView transcribedTextView = findViewById(R.id.transcriptDisplay);

        // Set the title in the TextView
        titleTextView.setText(title);

        // Initialize MediaPlayer
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare(); // Prepare the MediaPlayer
            Log.d(TAG, "MediaPlayer prepared successfully.");
        } catch (Exception e) {
            Log.e(TAG, "Error preparing MediaPlayer: " + e.getMessage());
        }

        // Check for permissions
        checkPermissions();

        // Set up play/pause functionality for the ImageButton
        playButton.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause(); // Pause the audio if it's playing
                playButton.setImageResource(android.R.drawable.ic_media_play); // Change button icon to play
                Log.d(TAG, "Audio paused.");
            } else {
                mediaPlayer.start(); // Start the audio if it's paused
                playButton.setImageResource(android.R.drawable.ic_media_pause); // Change button icon to pause
                Log.d(TAG, "Audio started.");

                // Start the speech recognizer after the audio starts playing
                startSpeechRecognition();
            }
        });
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_AUDIO_PERMISSION_CODE);
        } else {
            Log.d(TAG, "Audio permission already granted.");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_AUDIO_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Audio recording permission granted.");
            } else {
                Log.e(TAG, "Audio recording permission denied.");
            }
        }
    }

    private void startSpeechRecognition() {
        // Create SpeechRecognizer instance
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.d(TAG, "Ready for speech.");
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.d(TAG, "Speech started.");
            }

            @Override
            public void onRmsChanged(float msdB) {
                // Optionally visualize the sound level
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                // Optional: Handle audio buffer
            }

            @Override
            public void onEndOfSpeech() {
                Log.d(TAG, "Speech ended.");
            }

            @Override
            public void onError(int error) {
                String errorMessage = "Error recognizing speech: " + error;
                Log.e(TAG, errorMessage);
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String transcribedText = matches.get(0);
                    TextView transcribedTextView = findViewById(R.id.transcriptDisplay);
                    transcribedTextView.setText(transcribedText);
                    Log.d(TAG, "Transcribed text: " + transcribedText);
                } else {
                    Log.e(TAG, "No recognition results found.");
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                // Handle partial results if necessary
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                // Handle events if necessary
            }
        });

        // Start listening
        speechRecognizer.startListening(speechRecognizerIntent);
        Log.d(TAG, "Started listening for speech.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Release MediaPlayer resources
            mediaPlayer = null; // Nullify the reference
        }
        if (speechRecognizer != null) {
            speechRecognizer.cancel(); // Cancel speech recognition
            speechRecognizer.destroy(); // Destroy the recognizer
        }
    }
}
