# 🎵 SangeetNow – AI-Powered Music Preview App 
<div align="center">
  <img src="https://nilayg26.github.io/Animation/musicapplogo.jpg" alt="SangeetNow Logo" width="300"/>
</div>

---

## 📌 Overview  
SangeetNow is an AI-enabled Android app that lets you play and download 30-second previews from millions of songs. Whether you’re a music lover looking to update your ringtone or a content creator in need of song clips for your projects, SangeetNow has you covered.  

## 🚀 How did I do it?

- Integrated Google OAuth using `Firebase Authentication` to enable secure "Continue with Google" login.
- Managed seamless list/player playback using just two `MediaPlayer` & for download I used `DownloadManager` class with user `BroadcastReciever` class providing download notifications.
- Fetched song data from the `Deezer Music API` using `Retrofit`, then loaded and played audio through `MediaPlayer`.
- Enabled AI-driven song selection by sending user mood prompts to `Gemini AI` via `Google’s AI Studio`, with selection logic coordinated through `ViewModels`.
- Created a responsive, `stateful` & `state-aware UI` using `Jetpack Compose` integrated with sophisticated `ViewModels` to provide robust backend integration.
- Integrated `Lottie animations` fetched from personally hosted `GitHub Pages`, then saved to device `ROM` using `SharedPreferences` to minimize mobile data usage.
## 🚀 What Major Technologies & Concepts I Used?  
`Kotlin`, `Jetpack Compose`, `Retrofit`, `Deezer API`, `MVVM`, `Google's AI Studio`, `Media Player Class`, `DownloadManager Class`, `BroadcastReceiver Class`, `Glide Image Library`, `Android SDK`
## 🌟 Potential Impact

Assists millions of content creators and YouTubers with AI-driven song clips, making music discovery and ringtone updates effortless.



## ✨ Features  
- **Clean & Modern UI** with carefully placed animation to enhance user experience.
- **30-Second Song Previews**: Play and download high-quality 30-second clips from millions of tracks.
- **AI-Powered Song Discovery**: Just prompt the app with phrases like *"happy song,"* *"Bollywood,"* or *"party music,"* and SangeetNow will play the perfect 30 second track for your mood.
- **Perfect for Content Creators**: Ideal for YouTubers, video editors, and social media creators looking for short music clips to enhance their projects.   
- **Easy Download & Playback**: Save clips effortlessly and listen to them anytime.  
- **Ringtone Maker**: Convert downloaded clips into ringtones with one tap.  

## 📸 Some Screenshots

| Login Page | Search Page | Search Results |
|------------|-------------|----------------|
| <img src="https://nilayg26.github.io/Animation/SangeetNowSamplePics.04jpeg.jpeg" width="200"/> | <img src="https://nilayg26.github.io/Animation/SangeetNowSamplePics.03.jpeg" width="200"/> | <img src="https://nilayg26.github.io/Animation/SangeetNowSamplePics.02.jpeg" width="200"/> |

| AI Prompt | AI Results | Music Player Page |
|-----------|------------|--------------------|
| <img src="https://nilayg26.github.io/Animation/SangeetNowSamplePics.07.jpeg" width="200"/> | <img src="https://nilayg26.github.io/Animation/SangeetNowSamplePics.10.jpeg" width="200"/> | <img src="https://nilayg26.github.io/Animation/SangeetNowSamplePics.01.jpeg" width="200"/> |

| Profile Page | 
|--------------|
| <img src="https://nilayg26.github.io/Animation/SangeetNowSamplePics.21.jpeg" width="200"/> |


## 📥 Download  
[Click to Download the latest version of SangeetNow](https://github.com/nilayg26/SangeetNow/releases/download/v1.1.2/SangeetNow.apk)  

