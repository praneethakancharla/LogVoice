package com.example.logvoice;

public class Recording {
    private String username;
    private String title;
    private String timestamp;
    private String filePath;

    // Default constructor required for calls to DataSnapshot.getValue(Recording.class)
    public Recording() {}

    public Recording(String username, String title, String timestamp, String filePath) {
        this.username = username;
        this.title = title;
        this.timestamp = timestamp;
        this.filePath = filePath;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getFilePath() {
        return filePath;
    }
}
