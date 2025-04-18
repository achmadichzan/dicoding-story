# 📸 Dicoding Story App

A simple Android app built using Jetpack Compose that allows users to view, upload, and detail stories with images and location data. This project is part of Dicoding’s final submission for the Android Intermediate path.

---

## ✨ Features

- 🔐 User Authentication (Login & Register)
- 🧾 Fetch Stories with Pagination (from Dicoding API)
- 📌 Story Detail View
- 📤 Upload Image with Description (from Camera or Gallery)
- 📍 Optional Location (Lat/Lon) in uploads
- ☁️ Uses REST API: [Dicoding Story API](https://story-api.dicoding.dev)

---

## 🛠️ Built With

- 🧱 Jetpack Compose (UI)
- 🧳 ViewModel
- 🔗 Retrofit & OkHttp (Networking)
- 📦 Koin (Dependency Injection)
- 📸 Coil (Image loading)
- 🧾 Ktor Client (Optional - for upload story)
- 📷 CameraX + Gallery Picker

---

## 🚀 Getting Started

### 📦 Requirements

- Android Studio Giraffe or newer
- Minimum SDK 23
- Kotlin 1.9+
- Internet connection

### 🧰 Setup

1. **Clone the Repository**
```bash
git clone https://github.com/your-username/dicoding-story.git
cd dicoding-story
```
2. **Open with Android Studio**
3. **Set up your API base URL and token handling if needed**

Note: The app uses Bearer <token> for authenticated requests, make sure to login or register to obtain one.

---

## ⚠️ Disclaimer
This project is for learning purposes and is not currently licensed under any open-source license.

---

## 🙌 Acknowledgments
- Dicoding Indonesia for the API & learning path
- Google & Android Open Source Projects
- Jetpack Compose & Ktor Communities
