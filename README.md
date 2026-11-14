# Musiq

Local music player for Android. Plays FLAC, WAV, MP3 and the other formats Android supports (AAC, M4A, OGG, OPUS and more).

## Features
- Scans all audio on the device and lists it
- Search by song, artist, album or format (type "flac" to see only FLAC files)
- Background playback with notification and lock-screen controls
- Detects wired headphones, USB headsets and Bluetooth earbuds as soon as they connect or disconnect, and shows them in the top bar
- Pauses automatically when headphones are unplugged or Bluetooth drops
- Needs Android 8.0 or newer

## Get the APK (no Android Studio needed)
1. Create a free GitHub account and a new repository.
2. Upload everything in this folder to it (keep the folder structure, including `.github`).
3. Open the **Actions** tab, choose **Build Musiq APK**, and press **Run workflow**.
4. After a few minutes open the finished run and download **Musiq-apk**. Unzip it to get the `.apk`.
5. Copy the APK to your phone, open it, and allow "install unknown apps" when asked.

## Build with Android Studio instead
Open this folder in Android Studio, wait for the sync, then **Build > Build APK(s)**.
The APK is written to `app/build/outputs/apk/debug/`.

This is a debug-signed APK, which is fine for installing on your own phone. A Play Store release needs a release signing key.
