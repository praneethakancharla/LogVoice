# Log Voice 🎙️

**Log Voice** is an Android application designed to simplify managing and transcribing voice notes. The app allows users to record, store, and playback voice notes from meetings, lectures, or other events. It also features automatic transcription of audio, making it easy to generate text from your voice recordings.

---

## 🚀 Features
- Record voice notes with timestamps and custom names.
- Store recordings securely with Firebase Real-Time Database.
- Playback of recorded audio files.
- Automatic transcription of recorded audio using **Google's Speech Recognition API**.
- Intuitive and user-friendly interface.

---

## 🛠️ Technologies Used
- **Android Studio** (Java, XML)
- **Firebase Real-Time Database** (Login, storing audio files, etc.)
- **Media Recorder library** (Voice recording)
- **Media Reader library** (Audio playback)
- **Google Speech Recognition API** (For transcription)

---

## 📝 Installation Steps

1. **Clone the repository**:
    ```bash
    git clone https://github.com/yourusername/log-voice.git
    ```

2. **Open in Android Studio**:
   - Open Android Studio.
   - Select **Open an existing project**.
   - Choose the `LogVoice/` directory.

3. **Set up Firebase**:
   - Go to the [Firebase Console](https://console.firebase.google.com/).
   - Create a new project or use an existing one.
   - Add Firebase to your Android app:
     - Download `google-services.json` from Firebase.
     - Place the file in the `app/` directory.

## 📁 Folder Structure
```plaintext
LogVoice/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── logvoice/
│   │   │   │           ├── java files
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   └── xml files
│   │   │   ├── values/
│   │   │   │   └── strings.xml
├── build/
├── gradle/
├── README.md
└── ...


