# SoloLevelingLauncher

SoloLevelingLauncher is a custom Android launcher app built with Kotlin, using modern Android architecture and Jetpack Compose for a smooth, customizable user experience.

## Features

- Custom home screen displaying installed apps in a grid
- App icons and digital clock UI components
- Theming support (custom colors, typography)
- Dependency injection for modular architecture
- Fast app launching and efficient app management

## Project Structure

```
app/
 ├── src/
 │    └── main/
 │         ├── java/com/sumit/sololevelinglauncher/
 │         │     ├── di/                # Dependency injection setup
 │         │     ├── launcher/          # Application and home screen logic
 │         │     │     ├── LauncherApp.kt
 │         │     │     └── homescreen/
 │         │     │           └── MainActivity.kt
 │         │     │           └── component/   # UI components (AppGrid, AppIcon, DigitalClock)
 │         │     └── ui/
 │         │           ├── model/       # Data models (AppInfo)
 │         │           ├── presentation/# ViewModels (AppsViewModel)
 │         │           └── theme/       # Theming (Color, Theme, Type)
 │         ├── res/                    # Resources (drawables, icons, themes)
 │         └── AndroidManifest.xml
 └── build.gradle.kts
```

## Technologies Used

- **Kotlin**
- **Jetpack Compose**
- **AndroidX Libraries**
- **Dependency Injection** (Hilt or Dagger)
- **Gradle**

## Getting Started

1. Clone the repository.
2. Open in Android Studio.
3. Build and run on an Android device or emulator.

## License

This project is for educational purposes. See the [LICENSE.md](LICENSE.md) file for details.