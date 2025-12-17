# 🌍 TourismApp

TourismApp is an Android application that showcases tourism destinations with an
**offline-first approach** and **clean Android architecture**.

This project was developed as a **portfolio project** to demonstrate modern
Android development best practices.

---

## ✨ Features
- Tourism destinations categorized by type
- Detail screen for each destination
- Offline-first local database using Room
- Weather information using online API
- Modern UI built with Jetpack Compose

---

## 🧱 Architecture
The application follows a clean and scalable architecture:

- UI Layer (Jetpack Compose)
- ViewModel (State management)
- Repository (Data abstraction)
- Local Data Source (Room Database)
- Remote Data Source (Weather API)

---

## 🛠 Tech Stack
- Kotlin
- Jetpack Compose
- Room Database
- Retrofit
- MVVM Architecture
- Material 3

---

## 📂 Project Structure
app/
├─ data/
│ ├─ local/ # Room Database
│ ├─ remote/ # Weather API
│ └─ repository/ # Data abstraction
│
├─ ui/
│ ├─ home/
│ ├─ detail/
│ └─ welcome/
│
└─ MainActivity.kt

---

## 🚀 Getting Started
1. Clone this repository
2. Open the project in Android Studio
3. Sync Gradle
4. Run the app on emulator or physical device

> The application can run without an internet connection.  
> Weather feature requires an active internet connection.

---

## 🔮 Future Improvements
- Google Maps integration
- Online REST API backend
- Authentication system
- Pagination and caching

---

## 👤 Author
**Aldo Sepvenco**  
Android Developer (Entry Level)  
GitHub: https://github.com/aldosepvenco
